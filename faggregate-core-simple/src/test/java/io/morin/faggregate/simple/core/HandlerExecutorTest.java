package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.*;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HandlerExecutorTest {

    String identifier = "identifier";

    String state = "initial";

    @Mock
    Command command;

    ExecutionRequest<String, String, Command> request;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(identifier, state, command);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        val executor = new HandlerExecutor<>(
            request,
            (state, command) ->
                CompletableFuture.completedFuture(OutputBuilder.get(String.format("%s - %s", state, command)).build())
        );
        val output = executor.execute().get();
        assertNotNull(output);
        assertFalse(output.getResult().isEmpty());
        assertEquals("initial - command", output.getResult().get());
    }
}
