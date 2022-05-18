package todo.quarkus.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.morin.faggregate.api.Context;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;
import todo.model.command.TodoList;

@ApplicationScoped
@Slf4j
public class MongodbCommandRepository implements TodoListRepository {

    static String DATABASE_NAME = "todo";
    static String COLLECTION_NAME_STATE = "state";
    static String COLLECTION_NAME_EVENTS = "events";
    static String CONTEXT_KEY_VERSION = "version";

    @Inject
    MongoClient client;

    MongoCollection<StateRecord> getStateCollection() {
        return client.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME_STATE, StateRecord.class);
    }

    MongoCollection<EventRecord> getEventsCollection() {
        return client.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME_EVENTS, EventRecord.class);
    }

    @Override
    public <E> CompletableFuture<Void> persist(Context<TodoListId, ?> context, TodoList state, List<E> events) {
        return persist(context, state, events, false);
    }

    @SneakyThrows
    private <E> CompletableFuture<Void> persist(
        Context<TodoListId, ?> context,
        TodoList state,
        List<E> events,
        boolean deletion
    ) {
        val session = client.startSession();

        session.startTransaction();

        val aggregateStatePersister = new AggregateStatePersister(
            session,
            getStateCollection(),
            context,
            state,
            deletion
        );
        val aggregateEventsPersister = new AggregateEventsPersister<>(session, getEventsCollection(), context, events);

        return CompletableFuture
            .allOf(aggregateStatePersister.persist(), aggregateEventsPersister.persist())
            .whenComplete((result, throwable) -> {
                if (Objects.nonNull(throwable)) {
                    log.debug(String.format("unable to persist the aggregate %s", context.getIdentifier()), throwable);
                    session.abortTransaction();
                } else {
                    session.commitTransaction();
                }
            })
            .whenComplete((unused, throwable) -> {
                session.close();
            });
    }

    @Override
    public <E> CompletableFuture<Void> destroy(Context<TodoListId, ?> context, TodoList state, List<E> events) {
        log.debug("destroy state {} and event {}", state, events);
        return persist(context, state, events, true);
    }

    CompletableFuture<Optional<StateRecord>> load(TodoListId todoListId) {
        val idFilter = Filters.eq("body.todoListId.uuid", todoListId.getUuid().toString());
        val deletionFilter = Filters.eq("deleted", false);
        val filter = Filters.and(idFilter, deletionFilter);
        val record = getStateCollection().find().skip(0).limit(1).filter(filter).first();
        return CompletableFuture.completedFuture(Optional.ofNullable(record));
    }

    @Override
    public CompletableFuture<Optional<TodoList>> load(Context<TodoListId, ?> context) {
        return load(context.getIdentifier())
            .thenApply(o -> {
                o.ifPresent(stateRecord -> context.set(CONTEXT_KEY_VERSION, stateRecord.getVersion()));
                return o;
            })
            .thenApply(o -> o.map(StateRecord::getBody));
    }
}
