package io.morin.faggregate.api;

import java.util.List;
import java.util.Optional;

/**
 * An output provides the feedback of a command handling.
 *
 * @param <R> the type of the result, i.e. {@link Output#getResult()}
 */
public interface Output<R> {
    /**
     * The optional result
     *
     * @return the result
     */
    Optional<R> getResult();

    /**
     * The list of generated events.
     *
     * @return the events
     */
    List<Object> getEvents();
}
