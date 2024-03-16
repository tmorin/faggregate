package io.morin.faggregate.core.validation;

import io.morin.faggregate.api.Context;
import io.morin.faggregate.api.Initializer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * An in-memory repository is a repository that stores the state and the events in memory.
 * <p>
 * The purpose of this repository is to be used for testing and prototyping.
 * Especially in conjunction with the scenario execution.
 * So that, the scenario can be validated by the core business logic before to be used by the side effect implementations.
 * <p>
 * The initialization of the state can be handled by an initializer on load.
 * The <code>initializedWhenNoFound</code> flag must be set to true.
 * So that, the initializer will be called when the state is not found.
 *
 * @param <I> the type of the aggregate identifier
 * @param <S> the type of the aggregate state
 */
@Value
@Builder
public class InMemoryRepository<I, S> implements Suite.Repository<I, S> {

    /**
     * The map to store the states.
     */
    @Builder.Default
    Map<I, S> statesMap = new HashMap<>();

    /**
     * The map to store the events.
     */
    @Builder.Default
    Map<I, List<Object>> eventsMap = new HashMap<>();

    /**
     * The flag to initialize the state when the state is not found.
     * <p>
     * The default value is false.
     */
    boolean initializedWhenNoFound;

    /**
     * The initializer is called when the state is not found and the <code>initializedWhenNoFound</code> flag is set to true.
     * <p>
     * The default initializer throws an UnsupportedOperationException.
     */
    @Builder.Default
    Initializer<I, S> initializer = context -> {
        throw new UnsupportedOperationException("Not implemented");
    };

    @Override
    public <E> CompletableFuture<Void> persist(Context<I, ?> context, S state, List<E> events) {
        // Delegate the store method
        return this.storeState(context.getIdentifier(), state, events).toCompletableFuture();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<Optional<S>> load(Context<I, ?> context) {
        // Load the state
        return this.loadState(context.getIdentifier())
            .thenApply(d -> (Optional<S>) Optional.ofNullable(d))
            .thenCompose(optional -> {
                // If the state is not found and the initializedWhenNoFound flag is set to true, initialize the state
                if (optional.isEmpty() && initializedWhenNoFound) {
                    return initializer.initialize(context).thenApply(Optional::of);
                }
                return CompletableFuture.completedFuture(optional);
            })
            .toCompletableFuture();
    }

    @Override
    public <E> CompletableFuture<Void> destroy(Context<I, ?> context, S state, List<E> events) {
        // Remove the state
        statesMap.remove(context.getIdentifier());

        // Remove the events
        eventsMap.remove(context.getIdentifier());

        // Return a completed future
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletionStage<Void> storeState(
        @NonNull Object identifier,
        @NonNull Object state,
        @NonNull List<?> events
    ) {
        // Persist the state
        statesMap.put((I) identifier, (S) state);

        // Persist the events
        eventsMap.computeIfAbsent((I) identifier, k -> new ArrayList<>()).addAll(events);

        // Return a completed future
        return CompletableFuture.completedStage(null);
    }

    @Override
    public CompletionStage<Object> loadState(@NonNull Object identifier) {
        // Load the state
        return CompletableFuture.completedStage(statesMap.get(identifier));
    }
}
