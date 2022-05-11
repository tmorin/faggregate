package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Destroyer;
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

    final String identifier = "identifier";

    final String state = "initial";
    final String result = "result";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @Mock
    Serializable event0;

    Output<String> output;

    ExecutionResponse<String, String, Serializable, String> response;

    @Mock
    Destroyer<String, String> destroyer;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(ExecutionContext.create(identifier, command), state);
        output = OutputBuilder.get(result).add(event0).build();
        response = ExecutionResponse.create(request, output);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        Mockito
            .when(destroyer.destroy(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        StageDestroyAggregate.execute(response, destroyer).get();

        Mockito.verify(destroyer, Mockito.only()).destroy(Mockito.any(), Mockito.any(), Mockito.any());
    }
}
