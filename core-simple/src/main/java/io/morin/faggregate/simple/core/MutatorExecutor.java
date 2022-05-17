package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Mutator;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode
class MutatorExecutor<S, E> {

    @NonNull
    E event;

    @NonNull
    Mutator<S, E> mutator;

    @SuppressWarnings("unchecked")
    static <S, E> Mutator<S, E> castMutator(Mutator<S, ?> mutator) {
        return (Mutator<S, E>) mutator;
    }

    S execute(S state) {
        return this.mutator.mutate(state, event);
    }
}
