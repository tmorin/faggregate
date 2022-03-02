package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Event;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class MutatorsExecutor<I, S, C extends Command, R> {

    @NonNull
    ExecutionContext<I, S, C, R> context;

    @NonNull
    List<MutatorExecutor<S, Event>> executors;

    CompletableFuture<S> execute() {
        return CompletableFuture.completedFuture(
            executors.stream().reduce(context.getState(), (state, mutator) -> mutator.execute(state), (a, b) -> b)
        );
    }
}
