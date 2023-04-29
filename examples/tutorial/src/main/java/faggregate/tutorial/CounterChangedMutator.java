package faggregate.tutorial;

import io.morin.faggregate.api.Mutator;

/**
 * The ${@link CounterChangedMutator} Mutator mutates the state of the {@link Counter} Aggregate according to
 * the {@link CounterChangedMutator} Domain Event.
 */
class CounterChangedMutator implements Mutator<Counter, CounterChanged> {

    /**
     * Mutate the aggregate and return a new state.
     *
     * @param state the initial state
     * @param event the event
     * @return the mutated state
     */
    @Override
    public Counter mutate(Counter state, CounterChanged event) {
        return state.updateValue(event.getNewValue());
    }
}
