package faggregate.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CounterTest {

    static String DEFAULT_COUNTER_ID = "counter";
    Counter counter;

    @BeforeEach
    void setUp() {
        counter = Counter.create(DEFAULT_COUNTER_ID);
    }

    @Test
    void shouldHaveDefaultFields() {
        assertEquals(DEFAULT_COUNTER_ID, counter.getCounterId());
        assertEquals(0, counter.getValue());
    }

    @Test
    void shouldUpdateValue() {
        val newValue = 3;
        val newCounter = counter.updateValue(newValue);
        assertEquals(newValue, newCounter.getValue());
    }
}
