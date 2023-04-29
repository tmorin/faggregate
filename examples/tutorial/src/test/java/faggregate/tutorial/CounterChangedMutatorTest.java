package faggregate.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CounterChangedMutatorTest {

    static String DEFAULT_COUNTER_ID = "counter";
    Counter counter;
    CounterChangedMutator counterChangedMutator;

    @BeforeEach
    void setUp() {
        counter = Counter.create(DEFAULT_COUNTER_ID);
        counterChangedMutator = new CounterChangedMutator();
    }

    @Test
    void shouldMutateValue() {
        val counterChanged = new CounterChanged(DEFAULT_COUNTER_ID, 0, 1);
        val newState = counterChangedMutator.mutate(counter, counterChanged);
        assertEquals(counterChanged.getNewValue(), newState.getValue());
    }
}
