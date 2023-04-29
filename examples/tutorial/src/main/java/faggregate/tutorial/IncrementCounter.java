package faggregate.tutorial;

import lombok.Value;

/**
 * The command to increment a counter.
 */
@Value
public class IncrementCounter {

    /**
     * The identifier of the counter.
     */
    String counterId;
}
