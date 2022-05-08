package io.morin.faggregate.simple.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ExecutionRequest<I, S, C> {

    @NonNull
    I identifier;

    @NonNull
    S state;

    @NonNull
    C command;

    static <I, S, C> ExecutionRequest<I, S, C> create(I identifier, S state, C command) {
        return new ExecutionRequest<>(identifier, state, command);
    }
}
