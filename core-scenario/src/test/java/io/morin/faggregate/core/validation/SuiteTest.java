package io.morin.faggregate.core.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.Output;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SuiteTest {

    @Mock
    Scenario scenarioA;

    @Mock
    Scenario scenarioB;

    @Mock
    Scenario scenarioC;

    @Mock
    Scenario.Given given;

    @Mock
    Scenario.When when;

    @Mock
    Scenario.Then then;

    @Mock
    AggregateManager<Object> am;

    @Mock
    ScenarioExecutor.Before before;

    @Mock
    ScenarioExecutor.After after;

    @Mock
    Output<Object> output;

    @Test
    void shouldExecute() {
        when(scenarioA.getGiven()).thenReturn(given);
        when(scenarioA.getWhen()).thenReturn(when);
        when(scenarioA.getThen()).thenReturn(then);

        when(scenarioB.getGiven()).thenReturn(given);
        when(scenarioB.getWhen()).thenReturn(when);
        when(scenarioB.getThen()).thenReturn(then);

        when(scenarioC.getGiven()).thenReturn(given);
        when(scenarioC.getWhen()).thenReturn(when);
        when(scenarioC.getThen()).thenReturn(then);

        when(am.execute(any(), any())).thenReturn(CompletableFuture.completedFuture(output));
        when(after.fetch(any())).thenReturn(CompletableFuture.completedFuture(null));

        assertDoesNotThrow(() ->
            Suite
                .builder()
                .scenario(scenarioA)
                .scenario(scenarioB)
                .scenario(scenarioC)
                .build()
                .execute(am, before, after)
                .toCompletableFuture()
                .get()
        );

        verify(am, Mockito.times(3)).execute(any(), any());
        verify(after, Mockito.times(3)).fetch(any());

        verify(scenarioA, Mockito.times(4)).getGiven();
        verify(scenarioA, Mockito.times(1)).getWhen();
        verify(scenarioA, Mockito.times(3)).getThen();

        verify(scenarioB, Mockito.times(4)).getGiven();
        verify(scenarioB, Mockito.times(1)).getWhen();
        verify(scenarioB, Mockito.times(3)).getThen();

        verify(scenarioC, Mockito.times(4)).getGiven();
        verify(scenarioC, Mockito.times(1)).getWhen();
        verify(scenarioC, Mockito.times(3)).getThen();
    }

    @Test
    void shouldExecuteFailed() {
        when(scenarioA.getGiven()).thenReturn(given);
        when(scenarioA.getWhen()).thenReturn(when);

        when(am.execute(any(), any())).thenReturn(CompletableFuture.failedFuture(new Exception("exception")));

        assertThrows(
            ExecutionException.class,
            () -> Suite.builder().scenario(scenarioA).build().execute(am).toCompletableFuture().get()
        );
    }
}
