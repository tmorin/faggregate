package io.morin.faggregate.core.validation;

import io.morin.faggregate.api.AggregateManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.*;

/**
 * A suite is a set of scenario designed for the same {@link AggregateManager}.
 */
@Value
@Builder
public class Suite {

    /**
     * The set of scenarios.
     */
    @Singular
    List<Scenario> scenarios;

    /**
     * Execute the scenarios sequentially.
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
     * Execute the scenarios sequentially.
     * <p>
     * The before lambda is executed before the _Given_ phase.
     * Its purpose is to store the state of the aggregate.
     * As long as the state of the artifact is not provided during the _Given_ phase, the before lambda is not mandatory.
     * <p>
     * The after lambda is executed after the _Then_ phase.
     * Its purpose is to load the state of the aggregate.
     * As long as the state of the artifact is not validated during the _Then_ phase, the after lambda is not mandatory.
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
