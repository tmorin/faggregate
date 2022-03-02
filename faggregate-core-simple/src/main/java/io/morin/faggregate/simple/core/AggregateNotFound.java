package io.morin.faggregate.simple.core;

import lombok.NonNull;

/**
 * The exception means there is no state to load for a given identifier.
 */
public class AggregateNotFound extends Exception {

    <I> AggregateNotFound(@NonNull I identifier) {
        super(String.format("unable to load the aggregate %s", identifier));
    }
}
