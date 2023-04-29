package faggregate.tutorial;

import lombok.Value;

/**
 * The event states the value of a counter changed.
 */
@Value
public class CounterChanged {

    /**
     * The identifier of the counter.
     */
    String counterId;

    /**
     * The value before the change.
     */
    int oldValue;

    /**
     * The value after the change.
     */
    int newValue;
}
