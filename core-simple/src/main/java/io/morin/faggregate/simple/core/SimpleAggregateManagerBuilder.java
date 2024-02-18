package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
    final List<Middleware<I, ?, ?>> middlewares = new ArrayList<>();
    Initializer<I, S> initializer = context -> {
        throw new UnsupportedOperationException("Initializer not set");
    };
    Loader<I, S> loader = context -> {
        throw new UnsupportedOperationException("Loader not set");
    };
    Persister<I, S> persister = new Persister<>() {
        @Override
        public <E> CompletableFuture<Void> persist(Context<I, ?> context, S state, List<E> events) {
            throw new UnsupportedOperationException("Persister not set");
        }
    };
    Destroyer<I, S> destroyer = new Destroyer<>() {
        @Override
        public <E> CompletableFuture<Void> destroy(Context<I, ?> context, S state, List<E> events) {
            throw new UnsupportedOperationException("Destroyer not set");
        }
    };

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
    public AggregateManagerBuilder<I, S> set(@NonNull Initializer<I, S> initializer) {
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
        this.mutators.computeIfAbsent(type, aClass -> new ArrayList<>());
        this.mutators.get(type).add((Mutator<S, Object>) mutator);
        return this;
    }

    @Override
    public <C, R> AggregateManagerBuilder<I, S> add(Class<C> type, Handler<S, C, R> handler) {
        return this.add(type, handler, Intention.MUTATION);
    }

    @Override
    public <C, R> AggregateManagerBuilder<I, S> add(Middleware<I, C, R> middleware) {
        this.middlewares.add(middleware);
        return this;
    }

    @Override
    public AggregateManager<I> build() {
        return new SimpleAggregateManager<>(
            this.initializer,
            this.loader,
            this.persister,
            this.destroyer,
            this.handlers,
            this.mutators,
            this.middlewares
        );
    }
}
