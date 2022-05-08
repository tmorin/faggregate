package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Initializer;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageInitiateAggregate<I, S, C> {

    @NonNull
    I identifier;

    @NonNull
    C command;

    @NonNull
    Initializer<I, S> initializer;

    static <I, S, C> CompletableFuture<ExecutionRequest<I, S, C>> execute(
        @NonNull I identifier,
        @NonNull C command,
        @NonNull Initializer<I, S> initializer
    ) {
        return new StageInitiateAggregate<I, S, C>(identifier, command, initializer).execute();
    }

    CompletableFuture<ExecutionRequest<I, S, C>> execute() {
        return initializer
            .initialize(identifier)
            .thenComposeAsync(state ->
                CompletableFuture.completedFuture(new ExecutionRequest<>(identifier, state, command))
            );
    }
}
