package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Handler;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
class StageExecuteCommand<I, S, C, R> {

    @NonNull
    ExecutionRequest<I, S, C> request;

    @NonNull
    Handler<S, ?, ?> handler;

    static <I, S, C, R> CompletableFuture<ExecutionContext<I, S, C, R>> execute(
        @NonNull ExecutionRequest<I, S, C> request,
        @NonNull Handler<S, ?, ?> handler
    ) {
        return new StageExecuteCommand<I, S, C, R>(request, handler).execute();
    }

    CompletableFuture<ExecutionContext<I, S, C, R>> execute() {
        val castedHandler = HandlerExecutor.<S, C, R>castHandler(handler);
        val executor = new HandlerExecutor<>(request, castedHandler);
        return executor.execute().thenApplyAsync(output -> ExecutionContext.create(request, output));
    }
}
