package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.morin.faggregate.api.Event;
import io.morin.faggregate.api.Mutator;
import io.morin.faggregate.api.OutputBuilder;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StageMutateAggregateTest {

    String identifier = "identifier";

    String state = "initial";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @Mock
    Event event;

    String result = "result";

    Map<Class<?>, List<Mutator<String, ?>>> mutators = new HashMap<>();

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(identifier, state, command);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        mutators.put(
            event.getClass(),
            Collections.singletonList((state, event) -> String.format("%s - %s", state, event))
        );
        val context = StageMutateAggregate
            .execute(ExecutionContext.create(request, OutputBuilder.get(result).add(event).build()), mutators)
            .get();
        assertFalse(context.getOutput().getResult().isEmpty());
        assertEquals(result, context.getOutput().getResult().get());
        assertEquals("initial - event", context.getState());
        assertEquals(event, context.getOutput().getEvents().get(0));
    }
}
