package io.morin.faggregate.core.scenario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.morin.faggregate.api.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InMemoryRepositoryTest {

    @Mock
    Context<String, String> context;

    @Mock
    AggregateManagerBuilder<String, String> aggregateManagerBuilder;

    @Test
    @SneakyThrows
    void shouldPersist() {
        when(context.getIdentifier()).thenReturn("id");

        val repository = InMemoryRepository.<String, String>builder().build();

        repository.persist(context, "state", List.of("event_a")).get();

        assertEquals("state", repository.getStatesMap().get("id"));
        assertEquals(List.of("event_a"), repository.getEventsMap().get("id"));
    }

    @Test
    @SneakyThrows
    void shouldLoad() {
        when(context.getIdentifier()).thenReturn("id");

        val repository = InMemoryRepository
            .<String, String>builder()
            .statesMap(Map.of("id", "state"))
            .eventsMap(Map.of("id", List.of("event_a")))
            .build();

        val optionalState = repository.load(context).get();

        assertTrue(optionalState.isPresent());
        assertEquals("state", optionalState.get());
    }

    @Test
    @SneakyThrows
    void shouldNotLoad() {
        when(context.getIdentifier()).thenReturn("id");

        val repository = InMemoryRepository.<String, String>builder().build();

        val optionalState = repository.load(context).get();

        assertTrue(optionalState.isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldWithInitializer() {
        when(context.getIdentifier()).thenReturn("id");

        val repository = InMemoryRepository
            .<String, String>builder()
            .initializedWhenNoFound(true)
            .initializer(context -> CompletableFuture.completedFuture("state"))
            .build();

        val optionalState = repository.load(context).get();

        assertTrue(optionalState.isPresent());
        assertEquals("state", optionalState.get());
    }

    @Test
    @SneakyThrows
    void shouldNotLoadWithInitializerWhenNotActivated() {
        when(context.getIdentifier()).thenReturn("id");

        val repository = InMemoryRepository.<String, String>builder().initializedWhenNoFound(true).build();

        val exception = assertThrows(ExecutionException.class, () -> repository.load(context).get());

        assertEquals(UnsupportedOperationException.class, exception.getCause().getClass());
    }

    @Test
    @SneakyThrows
    void shouldDestroy() {
        when(context.getIdentifier()).thenReturn("id");

        val repository = InMemoryRepository
            .<String, String>builder()
            .statesMap(new HashMap<>(Map.of("id", "state")))
            .eventsMap(new HashMap<>(Map.of("id", List.of("event_a"))))
            .build();

        repository.destroy(context, "state_b", List.of("event_b")).get();

        assertFalse(repository.getStatesMap().containsKey("id"));
        assertFalse(repository.getEventsMap().containsKey("id"));
    }

    @Test
    void shouldApplyTo() {
        val repository = InMemoryRepository.<String, String>builder().build();

        assertNotNull(repository.applyTo(aggregateManagerBuilder));

        verify(aggregateManagerBuilder).set((Loader<String, String>) repository);
        verify(aggregateManagerBuilder).set((Persister<String, String>) repository);
        verify(aggregateManagerBuilder).set((Destroyer<String, String>) repository);
    }
}
