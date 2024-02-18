package io.morin.faggregate.core.validation;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.Output;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Execute {@link Scenario}.
 */
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScenarioExecutor {

    /**
     * The scenario to execute.
     */
    @NonNull
    Scenario scenario;

    /**
     * The aggregate manager to validate.
     */
    @NonNull
    AggregateManager<Object> am;

    /**
     * The action to perform before the command execution.
     */
    @NonNull
    Before before;

    /**
     * The action to perform after the command execution.
     */
    @NonNull
    After after;

    /**
     * The asserter to validate the state of the aggregate.
     */
    @Builder.Default
    StateAsserter stateAsserter = new DefaultStateAsserter();

    /**
     * The asserter to validate the events produced by the aggregate.
     */
    @Builder.Default
    EventsAsserter eventsAsserter = new DefaultEventsAsserter();

    /**
     * The processor to execute custom asserters.
     */
    @Builder.Default
    AssertersProcessor assertersProcessor = new DefaultAssertersProcessor();

    /**
     * Execute the scenario.
     * <ul>
     *     <li>Prepare the aggregate invoking {@link ScenarioExecutor#before}</li>
     *     <li>Execute the command on the aggregate</li>
     *     <li>Fetch the aggregate state invoking {@link ScenarioExecutor#before}</li>
     *     <li>Assert the expectation</li>
     * </ul>
     *
     * @return a completion stage
     */
    public CompletionStage<Void> execute() {
        return Optional
            // GIVEN STEP - initialize the state of the aggregate with a given state
            .ofNullable(scenario.getGiven().getState())
            .map(state -> before.store(scenario.getGiven().getIdentifier(), state, scenario.getGiven().getEvents()))
            .orElseGet(() -> CompletableFuture.completedStage(null))
            // GIVEN STEP - mutate the aggregate with given commands
            .thenAccept(unused -> {
                try {
                    for (Object command : scenario.getGiven().getCommands()) {
                        am.execute(scenario.getGiven().getIdentifier(), command).get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(e);
                }
            })
            // WHEN - mutate the aggregate with the tested command
            .thenCompose(unused -> am.execute(scenario.getGiven().getIdentifier(), scenario.getWhen().getCommand()))
            // WHEN - create the outcome based on the fetch aggregate state and the output of the command
            .thenCompose(output ->
                after
                    .load(scenario.getGiven().getIdentifier())
                    .thenApply(currentState -> new Outcome(output, currentState))
            )
            // THEN - assert the outcome
            .thenApply(outcome -> {
                // THEN - check actual state with the expected one
                Optional
                    .ofNullable(scenario.getThen().getState())
                    .ifPresent(expectedState ->
                        stateAsserter.process(
                            scenario,
                            Objects.requireNonNull(
                                outcome.state,
                                "The after lambda of the suite execution is not implemented!"
                            ),
                            expectedState
                        )
                    );
                // THEN - check actual events with the expected ones
                Optional
                    .ofNullable(scenario.getThen().getEvents())
                    .ifPresent(expectedEvents ->
                        eventsAsserter.process(scenario, outcome.output.getEvents(), expectedEvents)
                    );
                // THEN - check custom asserters
                Optional
                    .ofNullable(scenario.getThen().getAsserters())
                    .ifPresent(asserters -> assertersProcessor.process(scenario, outcome, asserters));
                return null;
            });
    }

    /**
     * Preparation of the aggregate.
     */
    @FunctionalInterface
    public interface Before {
        /**
         * Store the state of the aggregate.
         *
         * @param identifier the identifier of the aggregate
         * @param state      the initial state of the aggregate
         * @param events     a set of initial domain events
         * @return a completion stage
         */
        CompletionStage<Void> store(@NonNull Object identifier, @NonNull Object state, @NonNull List<?> events);
    }

    /**
     * Extraction of the aggregate state.
     */
    @FunctionalInterface
    public interface After {
        /**
         * Load the state of the aggregate.
         *
         * @param identifier the identifier of the aggregate
         * @return the state of the aggregate as a completion stage
         */
        CompletionStage<Object> load(@NonNull Object identifier);
    }

    /**
     * The outcome of a scenario execution.
     */
    @RequiredArgsConstructor
    static class Outcome {

        /**
         * The output of the Command Handler.
         */
        final Output<?> output;

        /**
         * The state fetched using {@link After#load(Object)}.
         */
        final Object state;
    }

    /**
     * The asserter to validate the state of the aggregate.
     */
    interface StateAsserter {
        /**
         * Assert the state of the aggregate.
         *
         * @param scenario      the scenario
         * @param actualState   the actual state of the aggregate
         * @param expectedState the expected state of the aggregate
         */
        void process(Scenario scenario, Object actualState, Object expectedState);
    }

    private static class DefaultStateAsserter implements StateAsserter {

        @Override
        @SuppressWarnings("java:S4274")
        public void process(@NonNull Scenario scenario, @NonNull Object actualState, @NonNull Object expectedState) {
            assert actualState.equals(expectedState) : String.format(
                "%s - the actual state doesn't match the expected one:%n%s%n%s",
                scenario.getName(),
                actualState,
                expectedState
            );
        }
    }

    /**
     * The asserter to validate the events produced by the aggregate.
     */
    interface EventsAsserter {
        /**
         * Assert the events produced by the aggregate.
         *
         * @param scenario       the scenario
         * @param actualEvents   the actual events of the aggregate
         * @param expectedEvents the expected events of the aggregate
         */
        void process(Scenario scenario, List<?> actualEvents, List<?> expectedEvents);
    }

    private static class DefaultEventsAsserter implements EventsAsserter {

        @Override
        @SuppressWarnings("java:S4274")
        public void process(
            @NonNull Scenario scenario,
            @NonNull List<?> actualEvents,
            @NonNull List<?> expectedEvents
        ) {
            // THEN - check the sizing of actual events with existing given one
            assert expectedEvents.size() == actualEvents.size() : String.format(
                "%s - the number of actual events (%s) doesn't match the expected ones (%s)",
                scenario.getName(),
                actualEvents.size(),
                expectedEvents.size()
            );
            // THEN - check actual events according to expected one
            var eventIndex = 0;
            for (val expectedEvent : expectedEvents) {
                val actualEvent = actualEvents.get(eventIndex);
                assert expectedEvent.equals(actualEvent) : String.format(
                    "%s - the actual event (%s) doesn't match the expected one (%s):%n%s%n%s",
                    scenario.getName(),
                    eventIndex,
                    eventIndex,
                    actualEvent,
                    expectedEvent
                );
                eventIndex++;
            }
        }
    }

    /**
     * The processor to execute custom asserters.
     */
    interface AssertersProcessor {
        /**
         * Process the custom asserters.
         *
         * @param scenario  the scenario
         * @param outcome   the outcome of the scenario execution
         * @param asserters the custom asserters
         */
        void process(Scenario scenario, Outcome outcome, List<BiConsumer<Object, List<?>>> asserters);
    }

    private static class DefaultAssertersProcessor implements AssertersProcessor {

        @Override
        public void process(
            @NonNull Scenario scenario,
            @NonNull Outcome outcome,
            @NonNull List<BiConsumer<Object, List<?>>> asserters
        ) {
            for (val asserter : asserters) {
                asserter.accept(outcome.state, outcome.output.getEvents());
            }
        }
    }
}
