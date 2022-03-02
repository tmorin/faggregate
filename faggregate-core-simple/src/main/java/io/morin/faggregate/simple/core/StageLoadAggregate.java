package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Loader;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageLoadAggregate<I, S, C extends Command> {

    @NonNull
    I identifier;

    @NonNull
    C command;

    @NonNull
    Loader<I, S> loader;

    static <I, S, C extends Command> CompletableFuture<ExecutionRequest<I, S, C>> execute(
        @NonNull I identifier,
        @NonNull C command,
        @NonNull Loader<I, S> loader
    ) {
        return new StageLoadAggregate<I, S, C>(identifier, command, loader).execute();
    }

    CompletableFuture<ExecutionRequest<I, S, C>> execute() {
        return loader
            .load(identifier)
            .thenComposeAsync(o ->
                o
                    .map(state -> CompletableFuture.completedFuture(new ExecutionRequest<>(identifier, state, command)))
                    .orElseGet(() -> CompletableFuture.failedFuture(new AggregateNotFound(identifier)))
            );
    }
}
