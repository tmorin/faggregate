package faggregate.tutorial;

import io.morin.faggregate.simple.core.SimpleAggregateManagerBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;
import org.junit.jupiter.api.Test;

/**
 * This JUnit test validates the side effect implementation executing the CounterSuite.
 */
class CounterSuiteTest {

    @Test
    void shouldValidateSideEffects() {
        // create the repository
        val counterRepository = CounterRepository.create();

        // create the builder for the aggregate manager
        val counterManagerBuilder = SimpleAggregateManagerBuilder.<String, Counter>get();

        // create the configurer and configure the aggregate manager
        // i.e. register the handlers, mutators and side effects
        CounterConfigurer.create(counterRepository).configure(counterManagerBuilder);

        // build the aggregate manager
        val counterManager = counterManagerBuilder.build();

        // execute the suite providing the aggregate manager
        CounterSuiteFactory
            .create()
            .execute(
                // the aggregate manager to test
                counterManager,
                // an optional lambda to store the initialize state of the aggregate before the _Given_ phase
                // this functionality is not required for the CounterSuite, so the implementation is null
                null,
                // an optional lambda to load the state of the aggregate before the _Then_ phase
                identifier -> CompletableFuture.completedFuture(counterRepository.statesByIdentifier.get(identifier))
            );
    }
}
