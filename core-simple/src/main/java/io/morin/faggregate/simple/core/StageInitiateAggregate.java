package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Initializer;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageInitiateAggregate<I, S, C> {

    @NonNull
    ExecutionContext<I, C> context;

    @NonNull
    Initializer<I, S> initializer;

    static <I, S, C> CompletableFuture<ExecutionRequest<I, S, C>> execute(
        @NonNull ExecutionContext<I, C> context,
        @NonNull Initializer<I, S> initializer
    ) {
        return new StageInitiateAggregate<I, S, C>(context, initializer).execute();
    }

    CompletableFuture<ExecutionRequest<I, S, C>> execute() {
        return initializer
            .initialize(context)
            .thenComposeAsync(state -> CompletableFuture.completedFuture(ExecutionRequest.create(context, state)));
    }
}
