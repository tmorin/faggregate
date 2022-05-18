package faggreat.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.AggregateManagerBuilder;
import io.morin.faggregate.simple.core.SimpleAggregateManagerBuilder;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CounterConfigurerTest {

    CounterRepository counterRepository;
    AggregateManager<String> counterManager;

    @BeforeEach
    void setUp() {
        final AggregateManagerBuilder<String, Counter> counterManagerBuilder = SimpleAggregateManagerBuilder.get();
        counterRepository = CounterRepository.create();
        val counterConfigurer = new CounterConfigurer(counterRepository);
        counterConfigurer.configure(counterManagerBuilder);
        counterManager = counterManagerBuilder.build();
    }

    @Test
    @SneakyThrows
    void shouldConfigure() {
        val counterId = "counter-0";
        counterManager.execute(counterId, new IncrementCounter(counterId)).get();
        assertEquals(1, counterRepository.statesByIdentifier.size());
    }
}
