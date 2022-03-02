package io.morin.faggregate.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A loader loads the state of an aggregate.
 *
 * @param <I> the type of the identifier
 * @param <S> the type of the state
 */
@FunctionalInterface
public interface Loader<I, S> {
    /**
     * Load the state of an aggregate.
     *
     * @param identifier the identifier
     * @return the state
     */
    CompletableFuture<Optional<S>> load(I identifier);
}
