package io.morin.faggregate.core.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.NoInteractions;
import org.mockito.internal.verification.Only;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScenarioExecutorTest {

    @Mock
    AggregateManager<Object> am;

    @Mock
    ScenarioExecutor.Before before;

    @Mock
    ScenarioExecutor.After after;

    String initialState = "John Doe";
    String expectedState = "JOHN DOE";
    UppercaseApplied expectedEvent = new UppercaseApplied(initialState, expectedState);
    UppercaseApplied wrongExpectedEvent = new UppercaseApplied(initialState, initialState);

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
                .given(Scenario.Given.builder().identifier("id").state(initialState).build())
                .when(Scenario.When.builder().command(new ApplyUppercase()).build())
                .then(Scenario.Then.builder().state(expectedState).event(expectedEvent).build())
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
        when(am.execute(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(after.invoke(any())).thenReturn(CompletableFuture.completedStage(expectedState));
        Assertions.assertDoesNotThrow(() -> executor.execute().toCompletableFuture().get());
        verify(before, new Only()).invoke(any(), any(), any());
    }

    @Test
    void shouldSuccessWhenInitialState() {
        when(am.execute(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(after.invoke(any())).thenReturn(CompletableFuture.completedStage(expectedState));
        Assertions.assertDoesNotThrow(() -> executorAlt.execute().toCompletableFuture().get());
        verify(before, new NoInteractions()).invoke(any(), any(), any());
    }

    @Test
    void shouldFailedWhenWrongExpectedState() {
        when(am.execute(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(expectedEvent).build()));
        when(after.invoke(any())).thenReturn(CompletableFuture.completedStage(initialState));

        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }

    @Test
    void shouldFailedWhenWrongExpectedEvent() {
        when(am.execute(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(OutputBuilder.get(null).add(wrongExpectedEvent).build()));
        when(after.invoke(any())).thenReturn(CompletableFuture.completedStage(expectedState));

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
        when(after.invoke(any())).thenReturn(CompletableFuture.completedStage(expectedState));

        Assertions.assertThrows(ExecutionException.class, () -> executor.execute().toCompletableFuture().get());
    }
}
