package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Loader;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageLoadAggregate<I, S, C> {

    @NonNull
    ExecutionContext<I, C> context;

    @NonNull
    Loader<I, S> loader;

    static <I, S, C> CompletableFuture<ExecutionRequest<I, S, C>> execute(
        @NonNull ExecutionContext<I, C> context,
        @NonNull Loader<I, S> loader
    ) {
        return new StageLoadAggregate<I, S, C>(context, loader).execute();
    }

    CompletableFuture<ExecutionRequest<I, S, C>> execute() {
        return loader
            .load(context)
            .thenComposeAsync(output ->
                output
                    .map(state -> CompletableFuture.completedFuture(ExecutionRequest.create(context, state)))
                    .orElseGet(() -> CompletableFuture.failedFuture(new AggregateNotFound(context.getIdentifier())))
            );
    }
}
