package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Destroyer;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageDestroyAggregate<I, S, C, R> {

    @NonNull
    ExecutionResponse<I, S, C, R> response;

    @NonNull
    Destroyer<I, S> destroyer;

    static <I, S, C, R> CompletableFuture<ExecutionResponse<I, S, C, R>> execute(
        @NonNull ExecutionResponse<I, S, C, R> response,
        @NonNull Destroyer<I, S> destroyer
    ) {
        return new StageDestroyAggregate<I, S, C, R>(response, destroyer).execute();
    }

    CompletableFuture<ExecutionResponse<I, S, C, R>> execute() {
        return destroyer
            .destroy(response, response.getState(), response.getOutput().getEvents())
            .thenApplyAsync(v -> response);
    }
}
