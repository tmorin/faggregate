package io.morin.faggregate.api;

/**
 * A {@link Configurer} configures a {@link AggregateManagerBuilder}.
 * <p>
 * The resources of an aggregate manager can be spread in many loose coupled modules.
 * For instance, one module contains the function implementation and another one the side effect implementation.
 * Therefore, because two modules have to configure the same aggregate manager,
 * this interface can be used to drive the implementation of the configurers but also their discovery.
 *
 * @param <I> the type of the aggregate identifier
 * @param <S> the type of the aggregate state
 */
@FunctionalInterface
public interface Configurer<I, S> {
    /**
     * Configure the given builder.
     *
     * @param builder the builder.
     */
    void configure(AggregateManagerBuilder<I, S> builder);
}
