package io.morin.faggregate.simple.core;

import io.morin.faggregate.api.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter(AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExecutionContext<I, C> implements Context<I, C> {

    I identifier;
    C command;
    Map<Object, Object> map = new HashMap<>();

    protected ExecutionContext(@NonNull ExecutionContext<I, C> context) {
        identifier = context.identifier;
        command = context.command;
        map.putAll(context.map);
    }

    public static <I, C> ExecutionContext<I, C> create(@NonNull I identifier, @NonNull C command) {
        return new ExecutionContext<>(identifier, command);
    }

    @Override
    public I getIdentifier() {
        return identifier;
    }

    @Override
    public C getCommand() {
        return command;
    }

    @Override
    public <K, V> Context<I, C> set(@NonNull K key, V value) {
        map.put(key, value);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Optional<V> get(@NonNull K key) {
        return (Optional<V>) Optional.ofNullable(map.get(key));
    }
}
