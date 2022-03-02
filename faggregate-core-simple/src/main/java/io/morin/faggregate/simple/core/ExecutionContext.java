package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Command;
import io.morin.faggregate.api.Output;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Getter(AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ExecutionContext<I, S, C extends Command, R> extends ExecutionRequest<I, S, C> {

    @NonNull
    Output<R> output;

    private ExecutionContext(@NonNull ExecutionRequest<I, S, C> request, Output<R> output) {
        super(request.getIdentifier(), request.getState(), request.getCommand());
        this.output = output;
    }

    static <I, S, C extends Command, R> ExecutionContext<I, S, C, R> create(
        @NonNull ExecutionRequest<I, S, C> request,
        Output<R> output
    ) {
        return new ExecutionContext<>(request, output);
    }

    ExecutionContext<I, S, C, R> withState(S state) {
        return new ExecutionContext<>(ExecutionRequest.create(getIdentifier(), state, getCommand()), output);
    }
}
