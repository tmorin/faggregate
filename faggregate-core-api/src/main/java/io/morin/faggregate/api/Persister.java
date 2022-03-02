package io.morin.faggregate.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A persister persists the state and/or the generate events of an aggregate.
 *
 * @param <I> the type of the identifier
 * @param <S> the type of the state
 */
@FunctionalInterface
public interface Persister<I, S> {
    /**
     * Persist the state and/or the generate events of an aggregate.
     *
     * @param identifier the identifier
     * @param state      the mutated state
     * @param events     the set of generated events
     * @param <E>        the type of the events
     * @return nothing
     */
    <E extends Event> CompletableFuture<Void> persist(I identifier, S state, List<E> events);
}
