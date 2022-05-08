package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class SimpleAggregateManager<I, S> implements AggregateManager<I> {

    @NonNull
    Initializer<S> initializer;

    @NonNull
    Loader<I, S> loader;

    @NonNull
    Persister<I, S> persister;

    @NonNull
    Destroyer<I, S> destroyer;

    @NonNull
    Map<Class<?>, Handler<S, ?, ?>> handlers;

    @NonNull
    Map<Class<?>, List<Mutator<S, Object>>> mutators;

    private <C, R> CompletableFuture<Output<R>> mutate(@NonNull ExecutionRequest<I, S, C> request) {
        // execute command
        return StageExecuteCommand
            .<I, S, C, R>execute(request, handlers)
            // apply mutations
            .thenComposeAsync(response -> StageMutateAggregate.execute(response, mutators))
            // persist state and events
            .thenComposeAsync(response -> StagePersistAggregate.execute(response, persister))
            .thenApply(ExecutionContext::getOutput);
    }

    private <C, R> CompletableFuture<Output<R>> destroy(@NonNull ExecutionRequest<I, S, C> request) {
        // execute command
        return StageExecuteCommand
            .<I, S, C, R>execute(request, handlers)
            // apply mutations
            .thenComposeAsync(response -> StageMutateAggregate.execute(response, mutators))
            // persist state and events
            .thenComposeAsync(response -> StageDestroyAggregate.execute(response, destroyer))
            .thenApply(ExecutionContext::getOutput);
    }

    @Override
    public <C, R> CompletableFuture<Output<R>> initiate(@NonNull I identifier, @NonNull C command) {
        return StageInitiateAggregate.execute(identifier, command, initializer).thenCompose(this::mutate);
    }

    @Override
    public <C, R> CompletableFuture<Output<R>> mutate(@NonNull I identifier, @NonNull C command) {
        return StageLoadAggregate.execute(identifier, command, loader).thenCompose(this::mutate);
    }

    @Override
    public <C, R> CompletableFuture<Output<R>> destroy(I identifier, C command) {
        return StageLoadAggregate.execute(identifier, command, loader).thenCompose(this::destroy);
    }
}
