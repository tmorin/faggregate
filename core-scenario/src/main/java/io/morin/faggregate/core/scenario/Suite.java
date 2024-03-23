package io.morin.faggregate.core.scenario;

import io.morin.faggregate.api.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.*;

/**
 * A suite is a set of scenario designed for the same {@link AggregateManager}.
 */
@Value
@Builder
public class Suite {

    /**
     * The repository is a set of interfaces to store, load and destroy the state of the aggregate.
     *
     * @param <I> the type of the identifier
     * @param <S> the type of the state
     */
    public interface Repository<I, S>
        extends Loader<I, S>, Persister<I, S>, Destroyer<I, S>, ScenarioExecutor.Before, ScenarioExecutor.After {
        /**
         * Apply the in-memory repository to the aggregate manager builder.
         *
         * @param builder the aggregate manager builder
         * @return the aggregate manager builder
         */
        default AggregateManagerBuilder<I, S> applyTo(@NonNull AggregateManagerBuilder<I, S> builder) {
            builder.set((Loader<I, S>) this);
            builder.set((Persister<I, S>) this);
            builder.set((Destroyer<I, S>) this);
            return builder;
        }
    }

    /**
     * The set of scenarios.
     */
    @Singular
    List<Scenario> scenarios;

    /**
     * The supplier of the in-memory repository.
     * <p>
     * The default value is a supplier that creates a new instance of the in-memory repository.
     */
    @Builder.Default
    Supplier<Repository<?, ?>> repositorySupplier = () -> InMemoryRepository.builder().build();

    /**
     * Execute the scenarios sequentially based on the given Aggregate Manager Builder.
     * <p>
     * The method build the artifact manager associating side effect implementations to an in-memory repository.
     * Moreover, the <i>before</i> and <i>after</i> lambda are implemented to store and load the state of the aggregate.
     * This method is useful to validate the scenarios before to be used by the side effect implementations.
     * That means to perform integration test on the core business logic, i.e. the {@link io.morin.faggregate.api.Handler}
     * and the {@link io.morin.faggregate.api.Mutator}.
     * <p>
     * The in-memory repository implements the following interfaces:
     * <ul>
     *     <li>{@link io.morin.faggregate.api.Persister}</li>
     *     <li>{@link io.morin.faggregate.api.Loader}</li>
     *     <li>{@link io.morin.faggregate.api.Destroyer}</li>
     *     <li>{@link io.morin.faggregate.core.scenario.ScenarioExecutor.Before#storeState(Object, Object, List)}</li>
     *     <li>{@link io.morin.faggregate.core.scenario.ScenarioExecutor.After#loadState(Object)}</li>
     * </ul>
     *
     * @param amBuilder the Aggregate Manager Builder
     * @param <I>       The type of the identifier
     * @param <S>       The type of the state
     * @return a completion stage
     */
    @SuppressWarnings("unchecked")
    public <I, S> CompletableFuture<Void> execute(@NonNull AggregateManagerBuilder<I, S> amBuilder) {
        val repository = (Repository<I, S>) repositorySupplier.get();
        return execute(repository.applyTo(amBuilder).build(), repository, repository);
    }

    /**
     * Execute the scenarios sequentially based on the given Aggregate Manager.
     * <p>
     * This method is useful to validate the side effect implementations when the scenarios don't rely on both an initial
     * state and the validation of the final state.
     *
     * @param am  the Aggregate Manager
     * @param <I> The type of the identifier
     * @return a completion stage
     */
    @SneakyThrows
    public <I> CompletableFuture<Void> execute(@NonNull AggregateManager<I> am) {
        return execute(am, null, null);
    }

    /**
     * Execute the scenarios sequentially based on the given Aggregate Manager.
     * <p>
     * This method is useful to validate the side effect implementations when the scenarios rely on either an initial
     * state or the validation of the final state.
     * <p>
     * The before lambda is executed before the _Given_ phase.
     * Its purpose is to store the state of the aggregate.
     * As long as the state of the artifact is not provided during the <i>Given</i> phase (i.e. {@link io.morin.faggregate.core.scenario.Scenario.Given#state}),
     * the before lambda is not mandatory.
     * <p>
     * The after lambda is executed after the _Then_ phase.
     * Its purpose is to load the state of the aggregate.
     * As long as the state of the artifact is not validated during the <i>Then</i> phase (i.e. {@link io.morin.faggregate.core.scenario.Scenario.Then#state}),
     * the after lambda is not mandatory.
     *
     * @param am     the Aggregate Manager
     * @param before the optional before lambda
     * @param after  the optional after lambda
     * @param <I>    The type of the identifier
     * @return a completion stage
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <I> CompletableFuture<Void> execute(
        @NonNull AggregateManager<I> am,
        ScenarioExecutor.Before before,
        ScenarioExecutor.After after
    ) {
        val executors = scenarios
            .stream()
            .map(scenario ->
                ScenarioExecutor
                    .builder()
                    .scenario(scenario)
                    .am((AggregateManager<Object>) am)
                    .before(
                        Optional
                            .ofNullable(before)
                            .orElse((identifier, state, events) ->
                                CompletableFuture.failedFuture(
                                    new UnsupportedOperationException("No state storing is implemented.")
                                )
                            )
                    )
                    .after(Optional.ofNullable(after).orElse(identifier -> CompletableFuture.completedStage(null)))
                    .build()
            )
            .collect(Collectors.toList());

        for (val executor : executors) {
            executor.execute().toCompletableFuture().get();
        }

        return CompletableFuture.<Void>completedStage(null).toCompletableFuture();
    }
}
