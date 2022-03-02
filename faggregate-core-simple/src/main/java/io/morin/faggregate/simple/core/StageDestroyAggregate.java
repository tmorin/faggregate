package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Destroyer;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageDestroyAggregate<I, S, C extends Command, R> {

    @NonNull
    ExecutionContext<I, S, C, R> response;

    @NonNull
    Destroyer<I, S> destroyer;

    static <I, S, C extends Command, R> CompletableFuture<ExecutionContext<I, S, C, R>> execute(
        @NonNull ExecutionContext<I, S, C, R> response,
        @NonNull Destroyer<I, S> destroyer
    ) {
        return new StageDestroyAggregate<I, S, C, R>(response, destroyer).execute();
    }

    CompletableFuture<ExecutionContext<I, S, C, R>> execute() {
        return destroyer
            .destroy(response.getIdentifier(), response.getState(), response.getOutput().getEvents())
            .thenApply(v -> response);
    }
}
