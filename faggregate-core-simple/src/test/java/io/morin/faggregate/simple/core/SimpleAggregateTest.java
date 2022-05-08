package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.morin.faggregate.api.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleAggregateTest {

    String identifier = "identifier";

    @Mock
    Initializer<String> initializer;

    @Mock
    Loader<String, String> loader;

    @Mock
    Persister<String, String> persister;

    @Mock
    Destroyer<String, String> destroyer;

    EmptyCommand command = new EmptyCommand();

    @Mock
    Handler<String, EmptyCommand, String> handler;

    EmptyEvent event = new EmptyEvent();

    @Mock
    Mutator<String, EmptyEvent> mutator;

    AggregateManager<String> aggregateManager;

    @BeforeEach
    void beforeEach() {
        Mockito
            .lenient()
            .when(persister.persist(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(null));
        Mockito
            .lenient()
            .when(destroyer.destroy(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(null));
        Mockito
            .when(handler.handle(Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get("result").add(event).build()));
        Mockito.when(mutator.mutate(Mockito.any(), Mockito.any())).thenReturn("mutated state");

        aggregateManager =
            SimpleAggregateManagerBuilder
                .<String, String>get()
                .set(loader)
                .set(initializer)
                .set(persister)
                .set(destroyer)
                .add(EmptyCommand.class, handler)
                .add(EmptyEvent.class, mutator)
                .build();
    }

    @Test
    @SneakyThrows
    void shouldInitiate() {
        Mockito.when(initializer.initialize()).thenReturn(CompletableFuture.completedFuture("initial state"));

        val output = aggregateManager.initiate(identifier, command).get();

        assertFalse(output.getResult().isEmpty());
        assertEquals("result", output.getResult().get());

        Mockito.verify(initializer, Mockito.only()).initialize();
        Mockito.verify(loader, Mockito.never()).load(Mockito.any());
        Mockito.verify(persister, Mockito.only()).persist(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(destroyer, Mockito.never()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(handler, Mockito.only()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(mutator, Mockito.only()).mutate(Mockito.any(), Mockito.any());
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        Mockito
            .when(loader.load(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of("loaded state")));

        val output = aggregateManager.mutate(identifier, command).get();

        assertFalse(output.getResult().isEmpty());
        assertEquals("result", output.getResult().get());

        Mockito.verify(initializer, Mockito.never()).initialize();
        Mockito.verify(loader, Mockito.only()).load(Mockito.any());
        Mockito.verify(persister, Mockito.only()).persist(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(destroyer, Mockito.never()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(handler, Mockito.only()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(mutator, Mockito.only()).mutate(Mockito.any(), Mockito.any());
    }

    @Test
    @SneakyThrows
    void shouldDestroy() {
        Mockito
            .when(loader.load(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of("loaded state")));

        val output = aggregateManager.destroy(identifier, command).get();

        assertFalse(output.getResult().isEmpty());
        assertEquals("result", output.getResult().get());

        Mockito.verify(initializer, Mockito.never()).initialize();
        Mockito.verify(loader, Mockito.only()).load(Mockito.any());
        Mockito.verify(persister, Mockito.never()).persist(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(destroyer, Mockito.only()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(handler, Mockito.only()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(mutator, Mockito.only()).mutate(Mockito.any(), Mockito.any());
    }

    static class EmptyCommand {}

    static class EmptyEvent {}
}
