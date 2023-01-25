package todo.quarkus.repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import io.morin.faggregate.api.Context;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;

/**
 * The persister persists a list of domain events.
 *
 * @param <E> the type of the events
 */
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class AggregateEventsPersister<E> {

    ClientSession session;

    MongoCollection<EventRecord> collection;

    Context<TodoListId, ?> context;

    List<E> events;

    CompletableFuture<Void> persist() {
        val records = events
            .stream()
            .map(event -> new EventRecord(context.getIdentifier(), event.getClass().getName(), event))
            .collect(Collectors.toList());

        if (records.isEmpty()) {
            log.debug("no events to persist");
            return CompletableFuture.completedFuture(null);
        }

        log.debug("persist {}", records);
        val insertManyResult = collection.insertMany(session, records);

        log.debug("inserted ids {}", insertManyResult.getInsertedIds());
        return CompletableFuture.completedFuture(null);
    }
}
