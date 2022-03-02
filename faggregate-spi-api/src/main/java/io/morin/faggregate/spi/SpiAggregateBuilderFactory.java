package io.morin.faggregate.spi;

import io.morin.faggregate.api.AggregateManagerBuilder;
import java.util.Iterator;
import java.util.ServiceLoader;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * The factory creates {@link AggregateManagerBuilder} leveraging on the {@link ServiceLoader} mechanism.
 */
@Slf4j
public class SpiAggregateBuilderFactory {

    /**
     * Create and get a new factory.
     *
     * @return the factory
     */
    public static SpiAggregateBuilderFactory get() {
        return new SpiAggregateBuilderFactory();
    }

    /**
     * Create a new {@link AggregateManagerBuilder}.
     *
     * @param <I> the type of the identifier
     * @param <S> the type of the state
     * @return the builder
     */
    @SuppressWarnings({ "cast", "unchecked", "rawtypes" })
    public <I, S> AggregateManagerBuilder<I, S> create() {
        final ServiceLoader providers = ServiceLoader.load(SpiAggregateBuilderProvider.class);

        val iterator = (Iterator<SpiAggregateBuilderProvider<I, S>>) providers.iterator();

        if (!iterator.hasNext()) {
            throw new IllegalStateException("No AggregateBuilderProvider defined!");
        }

        val provider = iterator.next();

        if (iterator.hasNext()) {
            log.warn("More than one ContainerProvider are defined!");
        }

        return provider.get();
    }
}
