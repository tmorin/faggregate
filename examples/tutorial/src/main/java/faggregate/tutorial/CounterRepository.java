package faggregate.tutorial;

import io.morin.faggregate.api.Context;
import io.morin.faggregate.api.Loader;
import io.morin.faggregate.api.Persister;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * An in memory implementation of both side effects Loader and Persister.
 */
@RequiredArgsConstructor
class CounterRepository implements Loader<String, Counter>, Persister<String, Counter> {

    /**
     * The map stores the states of the aggregates.
     */
    final Map<String, Counter> statesByIdentifier;

    /**
     * The mao stores the domain events of the aggregates.
     */
    final Map<String, List<Object>> eventsByIdentifier;

    static CounterRepository create() {
        return new CounterRepository(new HashMap<>(), new HashMap<>());
    }

    @Override
    public CompletableFuture<Optional<Counter>> load(Context<String, ?> context) {
        val counter = statesByIdentifier.getOrDefault(context.getIdentifier(), Counter.create(context.getIdentifier()));
        return CompletableFuture.completedFuture(Optional.of(counter));
    }

    @Override
    public <E> CompletableFuture<Void> persist(Context<String, ?> context, Counter state, List<E> events) {
        statesByIdentifier.put(context.getIdentifier(), state);
        if (!eventsByIdentifier.containsKey(context.getIdentifier())) {
            eventsByIdentifier.put(context.getIdentifier(), new ArrayList<>());
        }
        eventsByIdentifier.get(context.getIdentifier()).addAll(events);
        return CompletableFuture.completedFuture(null);
    }
}
