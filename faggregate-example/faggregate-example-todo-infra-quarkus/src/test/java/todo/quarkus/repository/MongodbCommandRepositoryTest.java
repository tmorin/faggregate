package todo.quarkus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mongodb.client.MongoClient;
import io.morin.faggregate.simple.core.ExecutionContext;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.command.ImmutableTodoList;
import todo.model.event.*;

@QuarkusTest
class MongodbCommandRepositoryTest {

    @Inject
    MongodbCommandRepository mongodbCommandRepository;

    @Inject
    MongoClient client;

    EventCounter eventCounter;

    String label;
    TodoListId todoListId;
    TodoItemId todoItemId;
    TodoItemAddedEvt todoItemAddedEvt;
    TodoItemRemovedEvt todoItemRemovedEvt;
    TodoItemToggledEvt todoItemToggledEvt;
    TodoListCreatedEvt todoListCreatedEvt;
    TodoListDeletedEvt todoListDeletedEvt;

    @BeforeEach
    void BeforeEach() {
        eventCounter = new EventCounter(client);
        label = "label";
        todoListId = TodoListId.generate();
        todoItemId = TodoItemId.generate();
        todoItemAddedEvt = new TodoItemAddedEvt(todoListId, todoItemId, "added");
        todoItemRemovedEvt = new TodoItemRemovedEvt(todoListId, todoItemId);
        todoItemToggledEvt = new TodoItemToggledEvt(todoListId, todoItemId, true);
        todoListCreatedEvt = new TodoListCreatedEvt(todoListId, "created");
        todoListDeletedEvt = new TodoListDeletedEvt(todoListId);
    }

    @SneakyThrows
    @Test
    void persist() {
        eventCounter.count();
        val context = ExecutionContext.create(todoListId, "persist");
        val state = ImmutableTodoList.builder().todoListId(todoListId).label(label).build();
        val events = List.of(todoListCreatedEvt);
        mongodbCommandRepository.persist(context, state, events).get();
        eventCounter.count();
        Assertions.assertEquals(eventCounter.getDiff(), events.size());
    }

    @SneakyThrows
    @Test
    void load() {
        {
            val context = ExecutionContext.create(todoListId, "persist");
            val state = ImmutableTodoList.builder().todoListId(todoListId).label(label).build();
            mongodbCommandRepository.persist(context, state, Collections.emptyList()).get();
        }
        {
            val context = ExecutionContext.create(todoListId, "load");
            val todoList = mongodbCommandRepository.load(context).get();
            assertTrue(todoList.isPresent());
            assertEquals(todoListId, todoList.get().todoListId());
        }
    }

    @SneakyThrows
    @Test
    void loadEmptyWhenNotFound() {
        val context = ExecutionContext.create(todoListId, "load");
        val todoList = mongodbCommandRepository.load(context).get();
        assertTrue(todoList.isEmpty());
    }

    @SneakyThrows
    @Test
    void destroy() {
        {
            eventCounter.count();
            val context = ExecutionContext.create(todoListId, "persist");
            val state = ImmutableTodoList.builder().todoListId(todoListId).label(label).build();
            val events = List.of(todoListCreatedEvt);
            mongodbCommandRepository.persist(context, state, events).get();
            eventCounter.count();
            Assertions.assertEquals(eventCounter.getDiff(), events.size());
        }
        {
            eventCounter.count();
            val context = ExecutionContext.create(todoListId, "destroy").set("version", 1);
            val state = ImmutableTodoList.builder().todoListId(todoListId).label(label).build();
            val events = List.of(todoListDeletedEvt);
            mongodbCommandRepository.destroy(context, state, events).get();
            eventCounter.count();
            Assertions.assertEquals(eventCounter.getDiff(), events.size());
        }
    }
}
