package io.morin.faggregate.core.validation;

import io.morin.faggregate.api.AggregateManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.val;

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
     * Execute the scenario.
     * <ul>
     *     <li>Prepare the aggregate invoking {@link ScenarioExecutor#before}</li>
     *     <li>Execute the command on the aggregate</li>
     *     <li>Fetch the aggregate state invoking {@link ScenarioExecutor#before}</li>
     *     <li>Assert the expectation</li>
     * </ul>
     */
    public CompletionStage<Void> execute() {
        return Optional
            .ofNullable(scenario.getGiven().getState())
            .map(state -> before.invoke(scenario.getGiven().getIdentifier(), state, scenario.getGiven().getEvents()))
            .orElseGet(() -> CompletableFuture.completedStage(null))
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
            .thenCompose(unused -> am.execute(scenario.getGiven().getIdentifier(), scenario.getWhen().getCommand()))
            .thenCombine(
                after.invoke(scenario.getGiven().getIdentifier()),
                (output, currentState) -> {
                    // check the state
                    Optional
                        .ofNullable(scenario.getThen().getState())
                        .ifPresent(expectedState -> {
                            assert currentState.equals(expectedState) : String.format(
                                "%s - the expected state doesn't match the current one:%n%s%n%s",
                                scenario.getName(),
                                expectedState,
                                currentState
                            );
                        });
                    // check the events
                    val currentEvents = output.getEvents();
                    val expectedEvents = scenario.getThen().getEvents();
                    assert expectedEvents.size() == currentEvents.size() : String.format(
                        "%s - the number of expected events (%s) doesn't match the current ones (%s)",
                        scenario.getName(),
                        expectedEvents.size(),
                        currentEvents.size()
                    );
                    // check event by event
                    var index = 0;
                    for (val expectedEvent : expectedEvents) {
                        val currentEvent = currentEvents.get(index);
                        assert expectedEvent.equals(currentEvent) : String.format(
                            "%s - the expected event (%s) doesn't match the current one (%s):%n%s%n%s",
                            scenario.getName(),
                            index,
                            index,
                            expectedEvent,
                            currentEvent
                        );
                        index++;
                    }
                    return null;
                }
            );
    }

    /**
     * Preparation of the aggregate.
     */
    @FunctionalInterface
    public interface Before {
        /**
         * Initialize the state of the aggregate.
         *
         * @param identifier the identifier of the aggregate
         * @param state      the initial state of the aggregate
         * @param events     a set of initial domain events
         */
        CompletionStage<Void> invoke(@NonNull Object identifier, @NonNull Object state, @NonNull List<?> events);
    }

    /**
     * Extraction of the aggregate state.
     */
    @FunctionalInterface
    public interface After {
        /**
         * Fetch the state of the aggregate.
         *
         * @param identifier the identifier of the aggregate
         */
        CompletionStage<Object> invoke(@NonNull Object identifier);
    }
}
