package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.morin.faggregate.api.*;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleAggregateAltTest {

    final String identifier = "identifier";

    @Mock
    Initializer<String> initializer;

    @Mock
    Loader<String, String> loader;

    @Mock
    Persister<String, String> persister;

    @Mock
    Destroyer<String, String> destroyer;

    AggregateManager<String> aggregateManager;

    @Test
    void shouldFailedWhenHandlerNotFound() {
        aggregateManager =
            SimpleAggregateManagerBuilder
                .<String, String>get()
                .set(loader)
                .set(initializer)
                .set(persister)
                .set(destroyer)
                .build();
        assertThrows(ExecutionException.class, () -> aggregateManager.execute(identifier, "test").get());
    }
}
