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
    ExecutionContext<I, S, C, R> context;

    @NonNull
    Map<Class<?>, List<Mutator<S, ?>>> mutators;

    static <I, S, C, R> CompletableFuture<ExecutionContext<I, S, C, R>> execute(
        @NonNull ExecutionContext<I, S, C, R> context,
        @NonNull Map<Class<?>, List<Mutator<S, ?>>> mutators
    ) {
        return new StageMutateAggregate<I, S, C, R>(context, mutators).execute();
    }

    CompletableFuture<ExecutionContext<I, S, C, R>> execute() {
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

        return mutatorsExecutor.execute().thenApply(state -> context.withState(state));
    }
}
