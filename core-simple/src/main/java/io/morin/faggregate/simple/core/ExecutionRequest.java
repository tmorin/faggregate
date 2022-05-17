package io.morin.faggregate.simple.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Getter(AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ExecutionRequest<I, S, C> extends ExecutionContext<I, C> {

    @NonNull
    S state;

    ExecutionRequest(@NonNull ExecutionContext<I, C> context, S state) {
        super(context);
        this.state = state;
    }

    protected ExecutionRequest(ExecutionRequest<I, S, C> context) {
        super(context);
        state = context.state;
    }

    static <I, S, C> ExecutionRequest<I, S, C> create(@NonNull ExecutionContext<I, C> context, @NonNull S state) {
        return new ExecutionRequest<>(context, state);
    }
}
