package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.*;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.OutputBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StageExecuteCommandTest {

    String identifier = "identifier";

    String state = "initial";

    @Mock
    Command command;

    ExecutionRequest<String, String, Command> request;

    Map<Class<?>, Handler<String, ?, ?>> handlers = new HashMap<>();

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(identifier, state, command);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        final Handler<String, Command, String> handler = (state, command) ->
            CompletableFuture.completedFuture(OutputBuilder.get(String.format("%s - %s", state, command)).build());
        handlers.put(command.getClass(), handler);

        val output = StageExecuteCommand.execute(request, handlers).get();
        assertEquals("initial", output.getState());
        assertEquals(command, output.getCommand());
        assertFalse(output.getOutput().getResult().isEmpty());
        assertEquals("initial - command", output.getOutput().getResult().get());
    }

    @Test
    @SneakyThrows
    void shouldThrowWhenNoHandler() {
        assertThrows(ExecutionException.class, () -> StageExecuteCommand.execute(request, handlers).get());
    }
}
