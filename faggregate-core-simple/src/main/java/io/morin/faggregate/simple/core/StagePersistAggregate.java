package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Persister;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class StagePersistAggregate<I, S, C extends Command, R> {

    @NonNull
    ExecutionContext<I, S, C, R> response;

    @NonNull
    Persister<I, S> persister;

    static <I, S, C extends Command, R> CompletableFuture<ExecutionContext<I, S, C, R>> execute(
        @NonNull ExecutionContext<I, S, C, R> response,
        @NonNull Persister<I, S> persister
    ) {
        return new StagePersistAggregate<I, S, C, R>(response, persister).execute();
    }

    CompletableFuture<ExecutionContext<I, S, C, R>> execute() {
        return persister
            .persist(response.getIdentifier(), response.getState(), response.getOutput().getEvents())
            .thenApply(v -> response);
    }
}
