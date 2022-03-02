package todo.quarkus.repository;

import static org.junit.jupiter.api.Assertions.*;

import io.morin.faggregate.api.Event;
import java.util.Collections;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import todo.model.TodoListId;
import todo.model.command.ImmutableTodoList;
import todo.model.command.TodoList;

@ExtendWith(MockitoExtension.class)
class InMemoryTodoListCommandRepositoryTest {

    TodoListId todoListId = TodoListId.generate();

    TodoList todoList = ImmutableTodoList.builder().todoListId(todoListId).build();

    @Mock
    Event event;

    @Test
    @SneakyThrows
    void shouldLoad() {
        val repository = new InMemoryTodoListCommandRepository();
        repository.snapshots.put(todoListId, todoList);
        val optional = repository.load(todoListId).get();
        assertTrue(optional.isPresent());
        assertEquals(todoList, optional.get());
    }

    @Test
    @SneakyThrows
    void shouldPersist() {
        val repository = new InMemoryTodoListCommandRepository();
        repository.persist(todoListId, todoList, Collections.singletonList(event)).get();
        assertFalse(repository.snapshots.isEmpty());
        assertEquals(todoList, repository.snapshots.get(todoListId));
        assertFalse(repository.events.isEmpty());
        assertEquals(event, repository.events.get(todoListId).get(0));
    }

    @Test
    @SneakyThrows
    void shouldDestroy() {
        val repository = new InMemoryTodoListCommandRepository();
        repository.snapshots.put(todoListId, todoList);
        repository.destroy(todoListId, todoList, Collections.singletonList(event)).get();
        assertTrue(repository.snapshots.isEmpty());
        assertTrue(repository.events.isEmpty());
    }
}
