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
     * @param <E>     the type of the events
     * @param context the context of the execution
     * @param state   the mutated state
     * @param events  the set of generated events
     * @return nothing
     */
    <E> CompletableFuture<Void> persist(Context<I, ?> context, S state, List<E> events);
}
