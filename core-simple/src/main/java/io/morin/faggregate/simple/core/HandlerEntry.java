package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Intention;
import lombok.NonNull;
import lombok.Value;

@Value
class HandlerEntry<S> {

    @NonNull
    Handler<S, ?, ?> handler;

    @NonNull
    Intention intention;
}
