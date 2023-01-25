package io.morin.faggregate.simple.core;

import lombok.NonNull;

/**
 * The exception means there is no state to load for a given identifier.
 */
public class AggregateNotFound extends Exception {

    /**
     * Create a new instance.
     *
     * @param identifier the identifier of the aggregate
     * @param <I>        the type of the identifier
     */
    public <I> AggregateNotFound(@NonNull I identifier) {
        super(String.format("unable to load the aggregate %s", identifier));
    }
}
