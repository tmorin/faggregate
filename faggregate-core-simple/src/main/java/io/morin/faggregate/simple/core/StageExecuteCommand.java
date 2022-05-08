package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Handler;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
class StageExecuteCommand<I, S, C, R> {

    @NonNull
    ExecutionRequest<I, S, C> request;

    @NonNull
    Map<Class<?>, Handler<S, ?, ?>> handlers;

    static <I, S, C, R> CompletableFuture<ExecutionContext<I, S, C, R>> execute(
        @NonNull ExecutionRequest<I, S, C> request,
        @NonNull Map<Class<?>, Handler<S, ?, ?>> handlers
    ) {
        return new StageExecuteCommand<I, S, C, R>(request, handlers).execute();
    }

    CompletableFuture<ExecutionContext<I, S, C, R>> execute() {
        val handlerKey = request.getCommand().getClass();
        return Optional
            .ofNullable(this.handlers.get(handlerKey))
            .map(HandlerExecutor::<S, C, R>castHandler)
            .map(handler -> new HandlerExecutor<>(request, handler))
            .map(CompletableFuture::completedFuture)
            .orElseGet(() ->
                CompletableFuture.failedFuture(new HandlerNotFoundException(request.getCommand().getClass()))
            )
            .thenComposeAsync(HandlerExecutor::execute)
            .thenApply(output -> ExecutionContext.create(request, output));
    }
}
