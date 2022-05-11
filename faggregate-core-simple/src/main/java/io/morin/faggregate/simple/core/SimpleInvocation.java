package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Context;
import io.morin.faggregate.api.Middleware;
import io.morin.faggregate.api.Output;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SimpleInvocation<I, C, R> implements Middleware.Next<R> {

    Middleware.Next<R> next;

    @SuppressWarnings("unchecked")
    private static <I, C, R> List<Middleware<I, C, R>> reverse(List<Middleware<I, ?, ?>> middlewares) {
        val castedMiddlewares = new ArrayList<Middleware<I, C, R>>();
        middlewares
            .stream()
            .map(SimpleInvocation::castHandler)
            .forEach(objectMiddleware -> castedMiddlewares.add((Middleware<I, C, R>) objectMiddleware));
        Collections.reverse(castedMiddlewares);
        return castedMiddlewares;
    }

    @SuppressWarnings("unchecked")
    static <I, C, R> Middleware<I, C, R> castHandler(Middleware<I, ?, ?> handler) {
        return (Middleware<I, C, R>) handler;
    }

    static <I, C, R> CompletableFuture<Output<R>> execute(
        final List<Middleware<I, ?, ?>> middlewares,
        final Middleware.Next<R> commandInvocation,
        final Context<I, C> context
    ) {
        if (!middlewares.isEmpty()) {
            final List<Middleware<I, C, R>> reversedMiddlewares = reverse(middlewares);
            var descendantInvocation = new SimpleInvocation<>(() ->
                reversedMiddlewares.get(0).wrap(commandInvocation, context)
            );
            val otherMiddlewares = reversedMiddlewares.stream().skip(1).collect(Collectors.toList());
            for (val parentMiddleware : otherMiddlewares) {
                val nextInvocation = descendantInvocation;
                descendantInvocation = new SimpleInvocation<>(() -> parentMiddleware.wrap(nextInvocation, context));
            }
            return descendantInvocation.invoke().toCompletableFuture();
        }

        return commandInvocation.invoke().toCompletableFuture();
    }

    @Override
    public CompletionStage<Output<R>> invoke() {
        return next.invoke();
    }
}
