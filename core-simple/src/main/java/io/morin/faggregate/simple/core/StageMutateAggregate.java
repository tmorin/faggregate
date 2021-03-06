package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Mutator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
class StageMutateAggregate<I, S, C, R> {

    @NonNull
    ExecutionResponse<I, S, C, R> context;

    @NonNull
    Map<Class<?>, List<Mutator<S, Object>>> mutators;

    static <I, S, C, R> CompletableFuture<ExecutionResponse<I, S, C, R>> execute(
        @NonNull ExecutionResponse<I, S, C, R> context,
        @NonNull Map<Class<?>, List<Mutator<S, Object>>> mutators
    ) {
        return new StageMutateAggregate<I, S, C, R>(context, mutators).execute();
    }

    CompletableFuture<ExecutionResponse<I, S, C, R>> execute() {
        val list = context
            .getOutput()
            .getEvents()
            .stream()
            .map(event ->
                Optional
                    .ofNullable(this.mutators.get(event.getClass()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(MutatorExecutor::castMutator)
                    .map(mutator -> new MutatorExecutor<>(event, mutator))
                    .collect(Collectors.toList())
            )
            .flatMap(List::stream)
            .collect(Collectors.toList());

        val mutatorsExecutor = new MutatorsExecutor<>(context, list);

        return mutatorsExecutor.execute().thenApplyAsync(state -> context.withState(state));
    }
}
