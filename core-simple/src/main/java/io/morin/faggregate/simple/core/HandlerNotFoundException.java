package io.morin.faggregate.simple.core;

import lombok.NonNull;

/**
 * The exception means a command cannot be handled because there is no matching {@link io.morin.faggregate.api.Handler}.
 */
public class HandlerNotFoundException extends Exception {

    /**
     * @param type the type of the command
     */
    public HandlerNotFoundException(@NonNull Class<?> type) {
        super(String.format("unable to find the handler of the Command type %s", type));
    }
}
