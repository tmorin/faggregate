package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class HandlerExecutor<I, S, C extends Command, R> {

    @NonNull
    ExecutionRequest<I, S, C> request;

    @NonNull
    Handler<S, C, R> handler;

    @SuppressWarnings("unchecked")
    static <S, C extends Command, R> Handler<S, C, R> castHandler(Handler<S, ?, ?> handler) {
        return (Handler<S, C, R>) handler;
    }

    CompletableFuture<Output<R>> execute() {
        return this.handler.handle(request.getState(), request.getCommand());
    }
}
