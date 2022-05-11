package io.morin.faggregate.api;

import java.util.concurrent.CompletionStage;

/**
 * <p>A middleware is a function which wraps the handling of a command.
 *
 * <p>Middlewares can be stacked and are registered using {@link AggregateManagerBuilder#add(Middleware)}.
 *
 * <p>A middleware must either continue the handling process calling the {@link Next#invoke()} method
 * or break the cycle returning a custom {@link CompletionStage}.
 *
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface Middleware<I, C, R> {
    /**
     * A middleware.
     *
     * @param next    the next
     * @param context the context
     * @return the output
     */
    CompletionStage<Output<R>> wrap(Next<R> next, Context<I, C> context);

    /**
     * A pointer to next middleware of the stack.
     *
     * @param <R> the type of the result
     */
    @FunctionalInterface
    interface Next<R> {
        /**
         * Continue the command handling.
         *
         * @return the output
         */
        CompletionStage<Output<R>> invoke();
    }
}
