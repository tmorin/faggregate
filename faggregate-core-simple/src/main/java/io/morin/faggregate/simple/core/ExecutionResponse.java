package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Output;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Getter(AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ExecutionResponse<I, S, C, R> extends ExecutionRequest<I, S, C> {

    @NonNull
    Output<R> output;

    private ExecutionResponse(@NonNull ExecutionRequest<I, S, C> context, Output<R> output) {
        super(context);
        this.output = output;
    }

    static <I, S, C, R> ExecutionResponse<I, S, C, R> create(
        @NonNull ExecutionRequest<I, S, C> request,
        @NonNull Output<R> output
    ) {
        return new ExecutionResponse<>(request, output);
    }

    ExecutionResponse<I, S, C, R> withState(S state) {
        return new ExecutionResponse<>(new ExecutionRequest<>(this, state), output);
    }
}
