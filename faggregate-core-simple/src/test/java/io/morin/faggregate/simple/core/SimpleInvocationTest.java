package io.morin.faggregate.simple.core;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.morin.faggregate.api.Middleware;
import io.morin.faggregate.api.OutputBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

class SimpleInvocationTest {

    Middleware.Next<String> commandExecution = () ->
        CompletableFuture.completedStage(OutputBuilder.get("Done!").build());

    Middleware<String> middleware1 = next ->
        next.invoke().thenApply(o -> OutputBuilder.get(format("1 %s", o.getResult().orElse(""))).build());

    Middleware<String> middleware2 = next ->
        next.invoke().thenApply(o -> OutputBuilder.get(format("2 %s", o.getResult().orElse(""))).build());

    Middleware<String> middleware3 = next ->
        next.invoke().thenApply(o -> OutputBuilder.get(format("3 %s", o.getResult().orElse(""))).build());

    List<Middleware<?>> middlewares = Arrays.asList(middleware1, middleware2, middleware3);

    @Test
    @SneakyThrows
    void execute() {
        val output = SimpleInvocation.execute(middlewares, commandExecution).get();
        assertFalse(output.getResult().isEmpty());
        assertEquals("1 2 3 Done!", output.getResult().get());
    }
}
