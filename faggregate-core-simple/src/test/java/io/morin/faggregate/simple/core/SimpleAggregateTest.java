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

    final String identifier = "identifier";
    final EmptyCommandA commandA = new EmptyCommandA();
    final EmptyCommandB commandB = new EmptyCommandB();
    final EmptyCommandC commandC = new EmptyCommandC();
    final EmptyEvent event = new EmptyEvent();

    @Mock
    Initializer<String, String> initializer;

    @Mock
    Loader<String, String> loader;

    @Mock
    Persister<String, String> persister;

    @Mock
    Destroyer<String, String> destroyer;

    @Mock
    Handler<String, EmptyCommandA, String> handlerA;

    @Mock
    Handler<String, EmptyCommandB, String> handlerB;

    @Mock
    Handler<String, EmptyCommandC, String> handlerC;

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
        Mockito.when(mutator.mutate(Mockito.any(), Mockito.any())).thenReturn("mutated state");

        aggregateManager =
            SimpleAggregateManagerBuilder
                .<String, String>get()
                .set(loader)
                .set(initializer)
                .set(persister)
                .set(destroyer)
                .add(EmptyCommandA.class, handlerA, Intention.INITIALIZATION)
                .add(EmptyCommandB.class, handlerB)
                .add(EmptyCommandC.class, handlerC, Intention.DESTRUCTION)
                .add(EmptyEvent.class, mutator)
                .build();
    }

    @Test
    @SneakyThrows
    void shouldInitiate() {
        Mockito
            .when(initializer.initialize(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture("initial state"));

        Mockito
            .when(handlerA.handle(Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get("result").add(event).build()));

        val output = aggregateManager.execute(identifier, commandA).get();

        assertFalse(output.getResult().isEmpty());
        assertEquals("result", output.getResult().get());

        Mockito.verify(initializer, Mockito.only()).initialize(Mockito.any());
        Mockito.verify(loader, Mockito.never()).load(Mockito.any());
        Mockito.verify(persister, Mockito.only()).persist(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(destroyer, Mockito.never()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(handlerA, Mockito.only()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(handlerB, Mockito.never()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(handlerC, Mockito.never()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(mutator, Mockito.only()).mutate(Mockito.any(), Mockito.any());
    }

    @Test
    @SneakyThrows
    void shouldMutate() {
        Mockito
            .when(loader.load(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of("loaded state")));

        Mockito
            .when(handlerB.handle(Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get("result").add(event).build()));
        val output = aggregateManager.execute(identifier, commandB).get();

        assertFalse(output.getResult().isEmpty());
        assertEquals("result", output.getResult().get());

        Mockito.verify(initializer, Mockito.never()).initialize(Mockito.any());
        Mockito.verify(loader, Mockito.only()).load(Mockito.any());
        Mockito.verify(persister, Mockito.only()).persist(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(destroyer, Mockito.never()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(handlerA, Mockito.never()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(handlerB, Mockito.only()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(handlerC, Mockito.never()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(mutator, Mockito.only()).mutate(Mockito.any(), Mockito.any());
    }

    @Test
    @SneakyThrows
    void shouldDestroy() {
        Mockito
            .when(loader.load(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(Optional.of("loaded state")));

        Mockito
            .when(handlerC.handle(Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get("result").add(event).build()));
        val output = aggregateManager.execute(identifier, commandC).get();

        assertFalse(output.getResult().isEmpty());
        assertEquals("result", output.getResult().get());

        Mockito.verify(initializer, Mockito.never()).initialize(Mockito.any());
        Mockito.verify(loader, Mockito.only()).load(Mockito.any());
        Mockito.verify(persister, Mockito.never()).persist(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(destroyer, Mockito.only()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(handlerA, Mockito.never()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(handlerB, Mockito.never()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(handlerC, Mockito.only()).handle(Mockito.any(), Mockito.any());
        Mockito.verify(mutator, Mockito.only()).mutate(Mockito.any(), Mockito.any());
    }

    static class EmptyCommandA {}

    static class EmptyCommandB {}

    static class EmptyCommandC {}

    static class EmptyEvent {}
}
