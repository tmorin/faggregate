package todo.quarkus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.morin.faggregate.api.Context;
import io.morin.faggregate.simple.core.ExecutionContext;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.command.ImmutableTodoList;
import todo.model.command.TodoList;

@Slf4j
@QuarkusTest
class AggregateStatePersisterTest {

    @Inject
    MongoClient client;

    ClientSession session;

    MongoCollection<StateRecord> collection;

    TodoListId todoListId;
    Context<TodoListId, ?> context;

    TodoList state;

    boolean deletion;

    @SneakyThrows
    @BeforeEach
    void beforeEach() {
        collection =
            client
                .getDatabase(MongodbCommandRepository.DATABASE_NAME)
                .getCollection(MongodbCommandRepository.COLLECTION_NAME_STATE, StateRecord.class);
        todoListId = TodoListId.generate();
        context = ExecutionContext.create(todoListId, "persist");
        state = ImmutableTodoList.builder().todoListId(todoListId).label("label").build();
        deletion = false;
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
    private void persistCurrentStateRecord() {
        try {
            session = client.startSession();
            session.startTransaction();
            val aggregateStatePersister = new AggregateStatePersister(session, collection, context, state, deletion);
            aggregateStatePersister.persist().get();
            session.commitTransaction();
        } finally {
            session.close();
        }
    }

    @SneakyThrows
    private List<StateRecord> listCurrentStateRecord() {
        val idFilter = Filters.eq("body.todoListId.uuid", todoListId.getUuid().toString());
        return StreamSupport
            .stream(collection.find().filter(idFilter).spliterator(), false)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    @Test
    void persistNew() {
        persistCurrentStateRecord();
        Assertions.assertEquals(1, listCurrentStateRecord().size());
    }

    @SneakyThrows
    @Test
    void persistFailsWhenNotFound() {
        context.set("version", 1);
        val exception = assertThrows(ExecutionException.class, this::persistCurrentStateRecord);
        assertEquals(AggregateNotFound.class, exception.getCause().getClass());
    }

    @SneakyThrows
    @Test
    void persistFailsWhenAlreadyDeleted() {
        deletion = true;
        persistCurrentStateRecord();
        context.set("version", 1);
        deletion = false;
        val exception = assertThrows(ExecutionException.class, this::persistCurrentStateRecord);
        assertEquals(AggregateNotFound.class, exception.getCause().getClass());
    }

    @SneakyThrows
    @Test
    void persistFailsWhenOptimistLocking() {
        {
            persistCurrentStateRecord();
        }
        {
            context.set("version", 1);
            persistCurrentStateRecord();
        }
        {
            context.set("version", 1);
            val exception = assertThrows(ExecutionException.class, this::persistCurrentStateRecord);
            assertEquals(AggregateNotFound.class, exception.getCause().getClass());
        }
    }
}
