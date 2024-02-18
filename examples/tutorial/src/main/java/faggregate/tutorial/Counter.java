package faggregate.tutorial;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * {@link Counter} is an Entity which is also the Root Aggregate of the Counter Aggregate.
 * <p>
 * The class is <i>immutable</i> because of the usage of {@link Value} Lombok annotation.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Counter {

    /**
     * The default value.
     */
    static final int DEFAULT_VALUE = 0;
    /**
     * The identifier of the counter.
     */
    String counterId;
    /**
     * The current state of the counter.
     */
    int value;

    /**
     * The factory should be used to initialize a new aggregate.
     *
     * @param counterId the identifier
     * @return the new aggregate
     */
    static Counter create(String counterId) {
        return new Counter(counterId, DEFAULT_VALUE);
    }

    /**
     * Update the value of the counter.
     *
     * @param newValue the new value
     * @return a new instance
     */
    Counter updateValue(int newValue) {
        return new Counter(counterId, newValue);
    }
}
