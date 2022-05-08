package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.OutputBuilder;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StageExecuteCommandTest {

    final String identifier = "identifier";

    final String state = "initial";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(identifier, state, command);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        final Handler<String, Serializable, String> handler = (state, command) ->
            CompletableFuture.completedFuture(OutputBuilder.get(String.format("%s - %s", state, command)).build());

        val output = StageExecuteCommand.execute(request, handler).get();
        assertEquals("initial", output.getState());
        assertEquals(command, output.getCommand());
        assertFalse(output.getOutput().getResult().isEmpty());
        assertEquals("initial - command", output.getOutput().getResult().get());
    }
}
