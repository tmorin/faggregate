package faggreat.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncrementCounterHandlerTest {

    static String DEFAULT_COUNTER_ID = "counter";
    Counter counter;
    IncrementCounter incrementCounter;
    IncrementCounterHandler incrementCounterHandler;

    @BeforeEach
    void setUp() {
        counter = Counter.create(DEFAULT_COUNTER_ID);
        incrementCounter = new IncrementCounter(counter.getCounterId());
        incrementCounterHandler = new IncrementCounterHandler();
    }

    @Test
    @SneakyThrows
    void shouldProduceCounterChanged() {
        val expected = new CounterChanged(DEFAULT_COUNTER_ID, 0, 1);
        val output = incrementCounterHandler.handle(counter, incrementCounter).get();
        assertEquals(expected, output.getEvents().get(0));
    }
}
