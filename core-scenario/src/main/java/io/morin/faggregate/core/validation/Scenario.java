package io.morin.faggregate.core.validation;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/**
 * A scenario is based on the Gherkins format : <code>GIVEN ... WHEN ... THEN ...</code>.
 */
@Value
@Builder
public class Scenario {

    /**
     * The name of the scenario.
     */
    @NonNull
    String name;

    /**
     * The context of the scenario.
     */
    @NonNull
    Given given;

    /**
     * The trigger of the scenario.
     */
    @NonNull
    When when;

    /**
     * The expected outcome of the scenario.
     */
    @NonNull
    Then then;

    /**
     * The description of a scenario context.
     */
    @Value
    @Builder
    public static class Given {

        /**
         * The identifier of the aggregate.
         */
        @NonNull
        Object identifier;

        /**
         * An initial state.
         */
        Object state;

        /**
         * A set of initial events.
         */
        @Singular
        List<?> events;
    }

    /**
     * The description of the scenario trigger.
     */
    @Value
    @Builder
    public static class When {

        /**
         * The command to execute.
         */
        @NonNull
        Object command;
    }

    /**
     * The description of the outcome.
     */
    @Value
    @Builder
    public static class Then {

        /**
         * The expected state.
         */
        Object state;

        /**
         * The expected set of events.
         */
        @Singular
        List<?> events;
    }
}