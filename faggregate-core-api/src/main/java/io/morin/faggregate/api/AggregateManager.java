package io.morin.faggregate.api;

import java.util.concurrent.CompletableFuture;

/**
 * <p>A facade providing an interface to manage Aggregates.
 *
 * <p>The facade provides only one fundamental action: the execution of commands.
 * A command is an intention to either initialize, mutate or destroy an aggregate.
 * Each execution returns an {@link Output}.
 * It provides an optional result and a set of events.
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
     * Mutate a managed aggregate.
     *
     * @param identifier the identifier
     * @param command    the command
     * @param <C>        the type of the command
     * @param <R>        the type of the result
     * @return the output
     */
    <C, R> CompletableFuture<Output<R>> execute(I identifier, C command);
}
