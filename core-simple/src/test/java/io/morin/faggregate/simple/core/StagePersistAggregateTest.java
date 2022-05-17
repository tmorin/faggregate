package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import io.morin.faggregate.api.Persister;
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
class StagePersistAggregateTest {

    final String identifier = "identifier";

    final String state = "initial";
    final String result = "result";

    @Mock
    Serializable command;

    ExecutionRequest<String, String, Serializable> request;

    @Mock
    Serializable event0;

    Output<String> output;

    ExecutionResponse<String, String, Serializable, String> context;

    @Mock
    Persister<String, String> persister;

    @BeforeEach
    void beforeEach() {
        request = ExecutionRequest.create(ExecutionContext.create(identifier, command), state);
        output = OutputBuilder.get(result).add(event0).build();
        context = ExecutionResponse.create(request, output);
    }

    @Test
    @SneakyThrows
    void shouldExecute() {
        Mockito
            .when(persister.persist(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        StagePersistAggregate.execute(context, persister).get();

        Mockito.verify(persister, Mockito.only()).persist(Mockito.any(), Mockito.any(), Mockito.any());
    }
}
