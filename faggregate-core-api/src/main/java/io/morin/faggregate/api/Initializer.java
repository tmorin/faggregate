package io.morin.faggregate.api;

import java.util.concurrent.CompletableFuture;

/**
 * An initializer initializes the state of an aggregate.
 *
 * @param <S> the type of the state
 */
@FunctionalInterface
public interface Initializer<S> {
    /**
     * Provide the initial state of an aggregate.
     *
     * @return the state
     */
    CompletableFuture<S> initialize();
}
