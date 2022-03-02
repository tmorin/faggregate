package io.morin.faggregate.spi;

import io.morin.faggregate.api.AggregateManagerBuilder;
import java.util.function.Supplier;

/**
 * <p>A provider provides an implementation of {@link AggregateManagerBuilder}.
 * <p>So that, the factory {@link SpiAggregateBuilderFactory} can discover it using the {@link java.util.ServiceLoader} mechanism.
 *
 * @param <I> the type of the identifier
 * @param <S> the type of the state
 */
public interface SpiAggregateBuilderProvider<I, S> extends Supplier<AggregateManagerBuilder<I, S>> {}
