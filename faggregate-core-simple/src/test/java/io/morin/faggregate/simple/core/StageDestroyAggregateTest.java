package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Destroyer;
import io.morin.faggregate.api.Event;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StageDestroyAggregateTest {

    String identifier = "identifier";

    String state = "initial";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @Mock
    Event event0;

    String result = "result";

    Output<String> output;

    ExecutionContext<String, String, Serializable, String> context;

    @Mock
    Destroyer<String, String> destroyer;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(identifier, state, command);
        output = OutputBuilder.get(result).add(event0).build();
        context = ExecutionContext.create(request, output);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        Mockito
            .when(destroyer.destroy(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        StageDestroyAggregate.execute(context, destroyer).get();

        Mockito.verify(destroyer, Mockito.only()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
    }
}
