package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.io.Serializable;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MutatorsExecutorTest {

    String identifier = "identifier";

    String state = "initial";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @Mock
    Serializable event0;

    @Mock
    Serializable event1;

    @Mock
    Serializable event2;

    String result = "result";

    Output<String> output;

    ExecutionContext<String, String, Serializable, String> context;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(identifier, state, command);
        output = OutputBuilder.get(result).add(event0, event1, event2).build();
        context = ExecutionContext.create(request, output);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        val list = Arrays.asList(
            new MutatorExecutor<String, Object>(event0, (state, event) -> String.format("%s - %s", state, event)),
            new MutatorExecutor<String, Object>(event1, (state, event) -> String.format("%s - %s", state, event)),
            new MutatorExecutor<String, Object>(event2, (state, event) -> String.format("%s - %s", state, event))
        );

        val mutatorsExecutor = new MutatorsExecutor<>(context, list);
        val newState = mutatorsExecutor.execute().get();
        assertEquals("initial - event0 - event1 - event2", newState);
    }
}
