package io.morin.faggregate.simple.spi;

import io.morin.faggregate.api.AggregateManagerBuilder;
import io.morin.faggregate.simple.core.SimpleAggregateManagerBuilder;
import io.morin.faggregate.spi.SpiAggregateBuilderProvider;

/**
 * The provider provides instances of {@link SimpleAggregateManagerBuilder}.
 *
 * @param <I> the type of the identifier
 * @param <S> the type of the state
 */
public class SimpleSpiAggregateBuilderProvider<I, S> implements SpiAggregateBuilderProvider<I, S> {

    @Override
    public AggregateManagerBuilder<I, S> get() {
        return SimpleAggregateManagerBuilder.get();
    }
}
