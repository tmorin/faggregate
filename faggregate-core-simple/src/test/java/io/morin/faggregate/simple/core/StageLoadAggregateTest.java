package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.morin.faggregate.api.Loader;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StageLoadAggregateTest {

    String identifier = "identifier";

    String state = "initial";

    @Mock
    Serializable command;

    @Mock
    Loader<String, String> loader;

    @Test
    @SneakyThrows
    void shouldExecute() {
        Mockito.when(loader.load(Mockito.any())).thenReturn(CompletableFuture.completedFuture(Optional.of(state)));
        StageLoadAggregate.execute(identifier, command, loader).get();
        Mockito.verify(loader, Mockito.only()).load(Mockito.any());
    }

    @Test
    @SneakyThrows
    void shouldFailed() {
        Mockito.when(loader.load(Mockito.any())).thenReturn(CompletableFuture.completedFuture(Optional.empty()));
        assertThrows(ExecutionException.class, () -> StageLoadAggregate.execute(identifier, command, loader).get());
    }
}
