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

    final String identifier = "identifier";

    final String state = "initial";
    final String result = "result";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @Mock
    Serializable event0;

    @Mock
    Serializable event1;

    @Mock
    Serializable event2;

    Output<String> output;

    ExecutionResponse<String, String, Serializable, String> response;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(ExecutionContext.create(identifier, command), state);
        output = OutputBuilder.get(result).add(event0, event1, event2).build();
        response = ExecutionResponse.create(request, output);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        val list = Arrays.asList(
            new MutatorExecutor<String, Object>(event0, (state, event) -> String.format("%s - %s", state, event)),
            new MutatorExecutor<String, Object>(event1, (state, event) -> String.format("%s - %s", state, event)),
            new MutatorExecutor<String, Object>(event2, (state, event) -> String.format("%s - %s", state, event))
        );

        val mutatorsExecutor = new MutatorsExecutor<>(response, list);
        val newState = mutatorsExecutor.execute().get();
        assertEquals("initial - event0 - event1 - event2", newState);
    }
}
