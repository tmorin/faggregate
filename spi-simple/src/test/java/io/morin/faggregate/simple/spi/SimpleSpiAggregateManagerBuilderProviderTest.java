package io.morin.faggregate.simple.spi;

import io.morin.faggregate.simple.core.SimpleAggregateManagerBuilder;
import io.morin.faggregate.spi.SpiAggregateBuilderFactory;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleSpiAggregateManagerBuilderProviderTest {

    @Test
    void shouldGet() {
        val aggregateBuilder = SpiAggregateBuilderFactory.get().create();
        Assertions.assertNotNull(aggregateBuilder);
        Assertions.assertEquals(SimpleAggregateManagerBuilder.class, aggregateBuilder.getClass());
    }
}
