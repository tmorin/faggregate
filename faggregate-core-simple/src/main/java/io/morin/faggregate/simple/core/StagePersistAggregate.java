package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Persister;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StagePersistAggregate<I, S, C, R> {

    @NonNull
    ExecutionResponse<I, S, C, R> response;

    @NonNull
    Persister<I, S> persister;

    static <I, S, C, R> CompletableFuture<ExecutionResponse<I, S, C, R>> execute(
        @NonNull ExecutionResponse<I, S, C, R> response,
        @NonNull Persister<I, S> persister
    ) {
        return new StagePersistAggregate<I, S, C, R>(response, persister).execute();
    }

    CompletableFuture<ExecutionResponse<I, S, C, R>> execute() {
        return persister
            .persist(response, response.getState(), response.getOutput().getEvents())
            .thenApplyAsync(v -> response);
    }
}
