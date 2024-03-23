package faggregate.tutorial;

import io.morin.faggregate.core.scenario.Scenario;
import io.morin.faggregate.core.scenario.Suite;
import lombok.experimental.UtilityClass;
import lombok.val;

/**
 * The {@link CounterSuiteFactory} class is a factory class that creates a suite of scenarios to validate the
 * behavior of the {@link Counter} Aggregate.
 */
@UtilityClass
public class CounterSuiteFactory {

    /**
     * Creates a suite of scenarios to validate the behavior of the {@link Counter} Aggregate.
     *
     * @return the suite of scenarios
     */
    public Suite create() {
        // Define a scenario validating the creation of a counter
        val scenarioAId = "counter#a";
        val scenarioA = Scenario
            .builder()
            .name("Counter Creation")
            // no state initialization is required for the Counter Creation scenario
            .given(Scenario.Given.builder().identifier(scenarioAId).build())
            // trigger the increment of the counter
            .when(Scenario.When.builder().command(new IncrementCounter(scenarioAId)).build())
            .then(
                Scenario.Then
                    .builder()
                    // validate the state of the aggregate has been incremented
                    .state(Counter.create(scenarioAId).updateValue(1))
                    // validate the right event has been published
                    .event(new CounterChanged(scenarioAId, 0, 1))
                    .build()
            )
            .build();

        // Define a scenario validating the increment of a counter
        val scenarioBId = "counter#b";
        val scenarioB = Scenario
            .builder()
            .name("Counter Creation")
            // initialize the state of the aggregate with a value of 1 for the Counter Increment scenario
            .given(Scenario.Given.builder().identifier(scenarioBId).command(new IncrementCounter(scenarioBId)).build())
            // trigger the increment of the counter
            .when(Scenario.When.builder().command(new IncrementCounter(scenarioBId)).build())
            .then(
                Scenario.Then
                    .builder()
                    // validate the state of the aggregate has been incremented
                    .state(Counter.create(scenarioBId).updateValue(2))
                    // validate the right event has been published
                    .event(new CounterChanged(scenarioBId, 1, 2))
                    .build()
            )
            .build();

        // Create and return a suite containing the two scenarios
        return Suite.builder().scenario(scenarioA).scenario(scenarioB).build();
    }
}
