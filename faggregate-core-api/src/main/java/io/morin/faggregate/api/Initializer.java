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
     * @param identifier the identifier of the aggregate
     * @return the state
     */
    CompletableFuture<S> initialize(I identifier);
}
