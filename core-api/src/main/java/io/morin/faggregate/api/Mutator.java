package io.morin.faggregate.api;

/**
 * A mutator mutates the state of an aggregate according to a given event.
 *
 * @param <S> the type of the state
 * @param <E> the type of the event
 */
@FunctionalInterface
public interface Mutator<S, E> {
    /**
     * Mutate the state according to an event.
     *
     * @param state the initial state
     * @param event the event
     * @return the mutated state
     */
    S mutate(S state, E event);
}
