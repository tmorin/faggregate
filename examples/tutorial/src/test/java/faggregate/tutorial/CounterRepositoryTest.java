package faggregate.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.morin.faggregate.simple.core.ExecutionContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CounterRepositoryTest {

    Map<String, Counter> states;
    Map<String, List<Object>> events;
    CounterRepository counterRepository;

    @BeforeEach
    void setUp() {
        states = new HashMap<>();
        events = new HashMap<>();
        counterRepository = new CounterRepository(states, events);
    }

    @Test
    @SneakyThrows
    void shouldLoadWhenUnknown() {
        val identifier = UUID.randomUUID().toString();
        val optionState = counterRepository.load(ExecutionContext.create(identifier, "command")).get();
        assertFalse(optionState.isEmpty());
        assertEquals(identifier, optionState.get().getCounterId());
    }

    @Test
    @SneakyThrows
    void shouldLoadWhenKnown() {
        val identifier = UUID.randomUUID().toString();
        val counter = Counter.create(identifier);
        states.put(identifier, counter);
        val context = ExecutionContext.create(identifier, "command");
        val optionState = counterRepository.load(context).get();
        assertFalse(optionState.isEmpty());
        assertEquals(counter, optionState.get());
    }

    @Test
    @SneakyThrows
    void shouldPersist() {
        val identifier = UUID.randomUUID().toString();
        val counter = Counter.create(identifier);
        val event = "event";
        val context = ExecutionContext.create(identifier, "command");
        counterRepository.persist(context, counter, List.of(event)).get();
        assertEquals(states.get(identifier), counter);
        assertEquals(events.get(identifier).get(0), event);
    }
}
