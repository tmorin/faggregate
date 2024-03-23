package io.morin.faggregate.core.scenario;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.OutputBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.NoInteractions;
import org.mockito.internal.verification.Only;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScenarioExecutorTest {

    @Mock
    AggregateManager<Object> am;

    @Mock
    ScenarioExecutor.Before before;

    @Mock
    ScenarioExecutor.After after;

    @Mock
    BiConsumer<Object, List<?>> asserter;

    String initialState = "John Doe";
    String expectedState = "JOHN DOE";
    UppercaseApplied expectedEvent = new UppercaseApplied(initialState, expectedState);
    UppercaseApplied wrongExpectedEvent = new UppercaseApplied(initialState, initialState);
    String commandA = "commandA";
    String commandB = "commandB";

    Scenario scenario;
    Scenario scenarioAlt;

    ScenarioExecutor executor;
    ScenarioExecutor executorAlt;

    @BeforeEach
    void setUp() {
        scenario =
            Scenario
                .builder()
                .name("scenario #1")
                .given(
                    Scenario.Given
                        .builder()
                        .identifier("id")
                        .state(initialState)
                        .command(commandA)
                        .command(commandB)
                        .build()
                )
                .when(Scenario.When.builder().command(new ApplyUppercase()).build())
                .then(Scenario.Then.builder().state(expectedState).event(expectedEvent).asserter(asserter).build())
                .build();
        scenarioAlt =
            Scenario
                .builder()
                .name("scenario #1")
                .given(Scenario.Given.builder().identifier("id").build())
                .when(Scenario.When.builder().command(new ApplyUppercase()).build())
                .then(Scenario.Then.builder().state(expectedState).event(expectedEvent).build())
                .build();
        executor = ScenarioExecutor.builder().am(am).scenario(scenario).before(before).after(after).build();
        executorAlt = ScenarioExecutor.builder().am(am).scenario(scenarioAlt).before(before).after(after).build();
    }

    @Test
    void shouldSuccess() {
        when(am.execute(eq("id"), any(ApplyUppercase.class)))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(am.execute(eq("id"), any(String.class)))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).build()));
        when(after.loadState(any())).thenReturn(CompletableFuture.completedStage(expectedState));
        Assertions.assertDoesNotThrow(() -> executor.execute().toCompletableFuture().get());
        verify(before, new Only()).storeState(eq("id"), eq(initialState), anyList());
        verify(am, new Times(2)).execute(eq("id"), anyString());
        verify(asserter, new Only()).accept(eq(expectedState), anyList());
    }

    @Test
    void shouldSuccessWhenInitialState() {
        when(am.execute(eq("id"), any(ApplyUppercase.class)))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(after.loadState(any())).thenReturn(CompletableFuture.completedStage(expectedState));
        Assertions.assertDoesNotThrow(() -> executorAlt.execute().toCompletableFuture().get());
        verify(before, new NoInteractions()).storeState(any(), any(), any());
        verify(am, new Only()).execute(eq("id"), any(ApplyUppercase.class));
    }

    @Test
    void shouldFailedWhenWrongExpectedState() {
        when(am.execute(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(after.loadState(any())).thenReturn(CompletableFuture.completedStage(initialState));

        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }

    @Test
    void shouldFailedWhenWrongExpectedEvent() {
        when(am.execute(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(wrongExpectedEvent).build()));
        when(after.loadState(any())).thenReturn(CompletableFuture.completedStage(expectedState));

        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }

    @Test
    void shouldFailedWhenTooMuchExpectedEvents() {
        when(am.execute(any(), any()))
            .thenReturn(
                CompletableFuture.completedFuture(
                    OutputBuilder.get(null).add(expectedEvent).add(wrongExpectedEvent).build()
                )
            );
        when(after.loadState(any())).thenReturn(CompletableFuture.completedStage(expectedState));

        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }

    @Test
    void shouldFailedWhenGivenCommandFailed() {
        when(am.execute("id", commandA)).thenReturn(CompletableFuture.failedFuture(new Exception()));
        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }

    @Test
    void shouldFailedWhenAsserterFails() {
        when(am.execute(eq("id"), any(ApplyUppercase.class)))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(am.execute(eq("id"), any(String.class)))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).build()));
        when(after.loadState(any())).thenReturn(CompletableFuture.completedStage(expectedState));
        doThrow(new IllegalStateException()).when(asserter).accept(any(), any());

        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }
}
