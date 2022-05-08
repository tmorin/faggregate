package io.morin.faggregate.api;

import java.util.concurrent.CompletableFuture;

/**
 * An handler handles a command to generate a set of {@link Event}.
 *
 * @param <S> the type of the state
 * @param <C> the type of the command
 * @param <R> the type of the result, i.e. {@link Output#getResult()}
 */
@FunctionalInterface
public interface Handler<S, C, R> {
    /**
     * Handle a command inspecting the aggregate state to generate a set side effects, i.e. {@link Event}.
     *
     * @param state   the current state of the aggregate
     * @param command the command
     * @return the handling output
     */
    CompletableFuture<Output<R>> handle(S state, C command);
}
