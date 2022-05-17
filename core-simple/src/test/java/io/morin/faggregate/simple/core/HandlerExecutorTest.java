package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.*;

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
class HandlerExecutorTest {

    final String identifier = "identifier";

    final String state = "initial";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(ExecutionContext.create(identifier, command), state);
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
