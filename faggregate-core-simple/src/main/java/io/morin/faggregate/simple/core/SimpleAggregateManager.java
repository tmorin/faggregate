package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class SimpleAggregateManager<I, S> implements AggregateManager<I> {

    @NonNull
    Initializer<I, S> initializer;

    @NonNull
    Loader<I, S> loader;

    @NonNull
    Persister<I, S> persister;

    @NonNull
    Destroyer<I, S> destroyer;

    @NonNull
    Map<Class<?>, HandlerEntry<S>> handlers;

    @NonNull
    Map<Class<?>, List<Mutator<S, Object>>> mutators;

    private <C, R> CompletableFuture<Output<R>> mutate(
        @NonNull ExecutionRequest<I, S, C> request,
        @NonNull Handler<S, ?, ?> handler
    ) {
        // execute command
        return StageExecuteCommand
            .<I, S, C, R>execute(request, handler)
            // apply mutations
            .thenComposeAsync(response -> StageMutateAggregate.execute(response, mutators))
            // persist state and events
            .thenComposeAsync(response -> StagePersistAggregate.execute(response, persister))
            .thenApplyAsync(ExecutionContext::getOutput);
    }

    private <C, R> CompletableFuture<Output<R>> destroy(
        @NonNull ExecutionRequest<I, S, C> request,
        @NonNull Handler<S, ?, ?> handler
    ) {
        // execute command
        return StageExecuteCommand
            .<I, S, C, R>execute(request, handler)
            // apply mutations
            .thenComposeAsync(response -> StageMutateAggregate.execute(response, mutators))
            // persist state and events
            .thenComposeAsync(response -> StageDestroyAggregate.execute(response, destroyer))
            .thenApplyAsync(ExecutionContext::getOutput);
    }

    @Override
    public <C, R> CompletableFuture<Output<R>> execute(I identifier, C command) {
        val handlerKey = command.getClass();
        if (!this.handlers.containsKey(handlerKey)) {
            return CompletableFuture.failedFuture(new HandlerNotFoundException(command.getClass()));
        }
        val handlerEntry = this.handlers.get(handlerKey);
        if (handlerEntry.getIntention().equals(Intention.INITIALIZATION)) {
            return StageInitiateAggregate
                .execute(identifier, command, initializer)
                .thenComposeAsync(request -> this.mutate(request, handlerEntry.getHandler()));
        }
        if (handlerEntry.getIntention().equals(Intention.DESTRUCTION)) {
            return StageLoadAggregate
                .execute(identifier, command, loader)
                .thenComposeAsync(request -> this.destroy(request, handlerEntry.getHandler()));
        }
        return StageLoadAggregate
            .execute(identifier, command, loader)
            .thenComposeAsync(request -> this.mutate(request, handlerEntry.getHandler()));
    }
}
