package io.morin.faggregate.simple.core;

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
class SimpleInvocation<R> implements Middleware.Next<R> {

    Middleware.Next<R> next;

    @Override
    public CompletionStage<Output<R>> invoke() {
        return next.invoke();
    }

    @SuppressWarnings("unchecked")
    private static <R> List<Middleware<R>> reverse(List<Middleware<?>> middlewares) {
        val castedMiddlewares = new ArrayList<Middleware<R>>();
        middlewares
            .stream()
            .map(SimpleInvocation::castHandler)
            .forEach(objectMiddleware -> castedMiddlewares.add((Middleware<R>) objectMiddleware));
        Collections.reverse(castedMiddlewares);
        return castedMiddlewares;
    }

    @SuppressWarnings("unchecked")
    static <R> Middleware<R> castHandler(Middleware<?> handler) {
        return (Middleware<R>) handler;
    }

    static <R> CompletableFuture<Output<R>> execute(
        final List<Middleware<?>> middlewares,
        final Middleware.Next<R> commandInvocation
    ) {
        if (!middlewares.isEmpty()) {
            final List<Middleware<R>> reversedMiddlewares = reverse(middlewares);
            var descendantInvocation = new SimpleInvocation<>(() -> reversedMiddlewares.get(0).wrap(commandInvocation));
            val otherMiddlewares = reversedMiddlewares.stream().skip(1).collect(Collectors.toList());
            for (val parentMiddleware : otherMiddlewares) {
                val nextInvocation = descendantInvocation;
                descendantInvocation = new SimpleInvocation<>(() -> parentMiddleware.wrap(nextInvocation));
            }
            return descendantInvocation.invoke().toCompletableFuture();
        }

        return commandInvocation.invoke().toCompletableFuture();
    }
}
