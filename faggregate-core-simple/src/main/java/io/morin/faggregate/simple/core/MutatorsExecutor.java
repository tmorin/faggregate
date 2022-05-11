package io.morin.faggregate.simple.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class MutatorsExecutor<I, S, C, R> {

    @NonNull
    ExecutionResponse<I, S, C, R> context;

    @NonNull
    List<MutatorExecutor<S, Object>> executors;

    CompletableFuture<S> execute() {
        return CompletableFuture.completedFuture(
            executors.stream().reduce(context.getState(), (state, mutator) -> mutator.execute(state), (a, b) -> b)
        );
    }
}
