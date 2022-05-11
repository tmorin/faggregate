package io.morin.faggregate.api;

import java.util.concurrent.CompletableFuture;

/**
 * An initializer initializes the state of an aggregate.
 *
 * @param <I> the type of the aggregate identifier
 * @param <S> the type of the aggregate state
 */
@FunctionalInterface
public interface Initializer<I, S> {
    /**
     * Provide the initial state of an aggregate.
     *
     * @param context the context of the execution
     * @return the state
     */
    CompletableFuture<S> initialize(Context<I, ?> context);
}
