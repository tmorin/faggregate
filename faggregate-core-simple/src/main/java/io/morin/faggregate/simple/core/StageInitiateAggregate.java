package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Initializer;
import io.morin.faggregate.api.Loader;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StageInitiateAggregate<I, S, C extends Command> {

    @NonNull
    I identifier;

    @NonNull
    C command;

    @NonNull
    Initializer<S> initializer;

    static <I, S, C extends Command> CompletableFuture<ExecutionRequest<I, S, C>> execute(
        @NonNull I identifier,
        @NonNull C command,
        @NonNull Initializer<S> initializer
    ) {
        return new StageInitiateAggregate<I, S, C>(identifier, command, initializer).execute();
    }

    CompletableFuture<ExecutionRequest<I, S, C>> execute() {
        return initializer
            .initialize()
            .thenComposeAsync(state ->
                CompletableFuture.completedFuture(new ExecutionRequest<>(identifier, state, command))
            );
    }
}
