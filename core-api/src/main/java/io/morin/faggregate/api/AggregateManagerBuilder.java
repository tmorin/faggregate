package io.morin.faggregate.api;

/**
 * An {@link AggregateManagerBuilder} builds {@link AggregateManager} instances.
 *
 * @param <I> the type of the aggregate identifier
 * @param <S> the type of the aggregate state
 */
public interface AggregateManagerBuilder<I, S> {
    /**
     * Set the aggregate initializer.
     *
     * @param initializer the initiator
     * @return the builder
     */
    AggregateManagerBuilder<I, S> set(Initializer<I, S> initializer);

    /**
     * Set the aggregate loader.
     *
     * @param loader the loader
     * @return the builder
     */
    AggregateManagerBuilder<I, S> set(Loader<I, S> loader);

    /**
     * Set the aggregate persister.
     *
     * @param persister the persister
     * @return the builder
     */
    AggregateManagerBuilder<I, S> set(Persister<I, S> persister);

    /**
     * Set the aggregate destroyer.
     *
     * @param destroyer the destroyer
     * @return the builder
     */
    AggregateManagerBuilder<I, S> set(Destroyer<I, S> destroyer);

    /**
     * Register a new command handler.
     *
     * @param <C>       the type of the command
     * @param <R>       the type of the result
     * @param type      the class of the command
     * @param handler   the command handler
     * @param intention the intention of the command
     * @return the builder
     */
    <C, R> AggregateManagerBuilder<I, S> add(Class<C> type, Handler<S, C, R> handler, Intention intention);

    /**
     * Register a new command handler with the intention {@link Intention#MUTATION}.
     *
     * @param <C>     the type of the command
     * @param <R>     the type of the result
     * @param type    the class of the command
     * @param handler the command handler
     * @return the builder
     */
    <C, R> AggregateManagerBuilder<I, S> add(Class<C> type, Handler<S, C, R> handler);

    /**
     * Register a new aggregate mutator.
     *
     * @param type    the class of the event
     * @param mutator the aggregate mutator
     * @param <E>     the type of the event
     * @return the builder
     */
    <E> AggregateManagerBuilder<I, S> add(Class<E> type, Mutator<S, E> mutator);

    /**
     * Register a new middleware.
     *
     * @param <C>        the type of the command
     * @param <R>        the type of the result
     * @param middleware the middleware
     * @return the builder
     */
    <C, R> AggregateManagerBuilder<I, S> add(Middleware<I, C, R> middleware);

    /**
     * Build and return a new {@link AggregateManager} instance.
     *
     * @return the instance
     */
    AggregateManager<I> build();
}
