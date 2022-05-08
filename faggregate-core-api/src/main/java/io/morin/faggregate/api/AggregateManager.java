package io.morin.faggregate.api;

import java.util.concurrent.CompletableFuture;

/**
 * <p>A facade providing an interface to manage Aggregates.
 *
 * <p>The facade provides three fundamental actions: 1. initiate, 2. mutate, 3. destroy.
 * An action is triggered by a command and returns an {@link Output}.
 * It provides an optional result and a set of {@link Event}.
 *
 * <p>Managers are built using {@link AggregateManagerBuilder}.
 * Each {@link AggregateManager} manages a DDD Aggregate.
 * That means, there is one {@link AggregateManager} by <i>DDD Root Aggregate</i>.
 *
 * <blockquote>A DDD aggregate is a cluster of domain objects that can be treated as a single unit. - Martin Fowler - <a href="https://www.martinfowler.com/bliki/DDD_Aggregate.html">https://www.martinfowler.com/bliki/DDD_Aggregate.html</a></blockquote>
 *
 * @param <I> the identifier type of the aggregate
 */
public interface AggregateManager<I> {
    /**
     * Initiate a new aggregate.
     * That means an aggregate which is not yet persisted.
     *
     * @param identifier the identifier of the new aggregate
     * @param command    the command triggering the initiation
     * @param <C>        the type of the command
     * @param <R>        the type of the result
     * @return the output
     */
    <C, R> CompletableFuture<Output<R>> initiate(I identifier, C command);

    /**
     * Mutate a managed aggregate.
     * @param identifier the identifier
     * @param command the command
     * @param <C>        the type of the command
     * @param <R>        the type of the result
     * @return the output
     */
    <C, R> CompletableFuture<Output<R>> mutate(I identifier, C command);

    /**
     * Destroy a managed aggregate.
     * @param identifier the identifier
     * @param command the command
     * @param <C>        the type of the command
     * @param <R>        the type of the result
     * @return the output
     */
    <C, R> CompletableFuture<Output<R>> destroy(I identifier, C command);
}
