package io.morin.faggregate.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * The builder is used to built {@link Output}.
 *
 * @param <R> the type of the result, i.e. {@link Output#getResult()}
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OutputBuilder<R> {

    R result;
    List<Object> events = new ArrayList<>();

    private OutputBuilder(R result) {
        this.result = result;
    }

    /**
     * Create a builder from an empty result, i.e. {@link Output#getResult()}.
     *
     * @return the builder
     */
    public static <Void> OutputBuilder<Void> get() {
        return new OutputBuilder<>(null);
    }

    /**
     * Create a builder from a given result, i.e. {@link Output#getResult()}.
     *
     * @param result the result
     * @param <R>    the type of the result, i.e. {@link Output#getResult()}
     * @return the builder
     */
    public static <R> OutputBuilder<R> get(R result) {
        return new OutputBuilder<>(result);
    }

    /**
     * Add events to the set of generated events.
     *
     * @param events a list of events
     * @return the builder
     */
    public OutputBuilder<R> add(Object... events) {
        this.events.addAll(Arrays.asList(events));
        return this;
    }

    /**
     * Build the {@link Output}.
     *
     * @return the output
     */
    public Output<R> build() {
        return new Output<>() {
            @Override
            public Optional<R> getResult() {
                return Optional.ofNullable(result);
            }

            @Override
            public List<Object> getEvents() {
                return events;
            }
        };
    }
}
