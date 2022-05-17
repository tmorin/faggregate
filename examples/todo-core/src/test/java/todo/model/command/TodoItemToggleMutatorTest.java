package todo.model.command;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.event.TodoItemToggledEvt;

class TodoItemToggleMutatorTest {

    final String label = "a label";
    final TodoItemId todoItemId0 = TodoItemId.generate();
    final TodoItemId todoItemId1 = TodoItemId.generate();
    final TodoItemId todoItemId2 = TodoItemId.generate();
    final TodoListId todoListId = TodoListId.generate();

    @Test
    void shouldMutate() {
        val todoItem0 = ImmutableTodoItem.builder().todoItemId(todoItemId0).label(label).isCompleted(true).build();
        val todoItem1 = ImmutableTodoItem.builder().todoItemId(todoItemId1).label(label).isCompleted(false).build();
        val todoItem2 = ImmutableTodoItem.builder().todoItemId(todoItemId2).label(label).isCompleted(false).build();
        val state = ImmutableTodoList
            .builder()
            .todoListId(todoListId)
            .addItems(todoItem0, todoItem1, todoItem2)
            .build();
        val TodoItemToggleMutator = new TodoItemToggledMutator();
        val newState = TodoItemToggleMutator.mutate(state, new TodoItemToggledEvt(todoListId, todoItemId1, true));
        assertEquals(3, newState.items().size());
        assertTrue(newState.items().get(0).isCompleted());
        assertTrue(newState.items().get(1).isCompleted());
        assertFalse(newState.items().get(2).isCompleted());
    }
}
