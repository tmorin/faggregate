package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * A simple implementation of the {@link AggregateManagerBuilder} interface.
 *
 * @param <I> the type of the identifier
 * @param <S> the type of the state
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleAggregateManagerBuilder<I, S> implements AggregateManagerBuilder<I, S> {

    final Map<Class<?>, HandlerEntry<S>> handlers = new HashMap<>();
    final Map<Class<?>, List<Mutator<S, Object>>> mutators = new HashMap<>();
    Initializer<S> initializer;
    Loader<I, S> loader;
    Persister<I, S> persister;
    Destroyer<I, S> destroyer;

    /**
     * Create and get a new builder.
     *
     * @param <I> the type of the identifier
     * @param <S> the type of the state
     * @return the builder
     */
    public static <I, S> AggregateManagerBuilder<I, S> get() {
        return new SimpleAggregateManagerBuilder<>();
    }

    @Override
    public AggregateManagerBuilder<I, S> set(@NonNull Initializer<S> initializer) {
        this.initializer = initializer;
        return this;
    }

    @Override
    public AggregateManagerBuilder<I, S> set(@NonNull Loader<I, S> loader) {
        this.loader = loader;
        return this;
    }

    @Override
    public AggregateManagerBuilder<I, S> set(@NonNull Persister<I, S> persister) {
        this.persister = persister;
        return this;
    }

    @Override
    public AggregateManagerBuilder<I, S> set(@NonNull Destroyer<I, S> destroyer) {
        this.destroyer = destroyer;
        return this;
    }

    @Override
    public <C, R> AggregateManagerBuilder<I, S> add(
        @NonNull Class<C> type,
        @NonNull Handler<S, C, R> handler,
        Intention intention
    ) {
        this.handlers.put(type, new HandlerEntry<>(handler, intention));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> AggregateManagerBuilder<I, S> add(@NonNull Class<E> type, @NonNull Mutator<S, E> mutator) {
        if (!this.mutators.containsKey(type)) {
            this.mutators.put(type, new ArrayList<>());
        }
        this.mutators.get(type).add((Mutator<S, Object>) mutator);
        return this;
    }

    @Override
    public <C, R> AggregateManagerBuilder<I, S> add(Class<C> type, Handler<S, C, R> handler) {
        return this.add(type, handler, Intention.MUTATION);
    }

    @Override
    public AggregateManager<I> build() {
        return new SimpleAggregateManager<>(
            this.initializer,
            this.loader,
            this.persister,
            this.destroyer,
            this.handlers,
            this.mutators
        );
    }
}
