package todo.quarkus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.morin.faggregate.api.Context;
import io.morin.faggregate.simple.core.ExecutionContext;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.event.TodoListCreatedEvt;

@Slf4j
@QuarkusTest
class AggregateEventsPersisterTest {

    @Inject
    MongoClient client;

    EventCounter eventCounter;

    ClientSession session;

    MongoCollection<EventRecord> collection;

    TodoListId todoListId;
    Context<TodoListId, ?> context;

    @SneakyThrows
    @BeforeEach
    void beforeEach() {
        eventCounter = new EventCounter(client);
        collection =
            client
                .getDatabase(MongodbCommandRepository.DATABASE_NAME)
                .getCollection(MongodbCommandRepository.COLLECTION_NAME_EVENTS, EventRecord.class);
        todoListId = TodoListId.generate();
        context = ExecutionContext.create(todoListId, "persist");
    }

    @AfterEach
    void afterEach() {
        if (Objects.nonNull(session)) {
            if (session.hasActiveTransaction()) {
                session.abortTransaction();
            }
            session.close();
        }
    }

    @SneakyThrows
    @Test
    void persist() {
        try {
            eventCounter.count();
            session = client.startSession();
            val aggregateEventsPersister = new AggregateEventsPersister<>(
                session,
                collection,
                context,
                List.of(new TodoListCreatedEvt(todoListId, "created"))
            );
            aggregateEventsPersister.persist().get();
            session.startTransaction();
            session.commitTransaction();
            eventCounter.count();
            assertEquals(1, eventCounter.getDiff());
        } finally {
            session.close();
        }
    }
}
