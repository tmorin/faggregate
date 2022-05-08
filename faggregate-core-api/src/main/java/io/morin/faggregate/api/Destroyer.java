package io.morin.faggregate.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A destroyer destroys the stateful data related to an aggregate.
 *
 * @param <I> the type of the identifier
 * @param <S> the type of the state
 */
@FunctionalInterface
public interface Destroyer<I, S> {
    /**
     * Destroy the stateful data of an aggregate
     *
     * @param identifier the identifier
     * @param state      the latest state
     * @param events     the events generated during the command handling
     * @param <E>        the type of the event
     * @return nothing
     */
    <E> CompletableFuture<Void> destroy(I identifier, S state, List<E> events);
}
