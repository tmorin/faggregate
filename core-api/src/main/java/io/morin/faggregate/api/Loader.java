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
     * @param context the context of the execution
     * @return the state
     */
    CompletableFuture<Optional<S>> load(Context<I, ?> context);
}
