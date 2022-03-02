package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.val;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.event.TodoItemAddedEvt;

class TodoItemAddedMutatorTest {

    String label = "a label";
    TodoItemId todoItemId = TodoItemId.generate();
    TodoListId todoListId = TodoListId.generate();

    @Test
    void shouldMutate() {
        val state = ImmutableTodoList.builder().todoListId(todoListId).build();
        val todoItemAddedMutator = new TodoItemAddedMutator();
        val newState = todoItemAddedMutator.mutate(state, new TodoItemAddedEvt(todoListId, todoItemId, label));
        assertFalse(newState.items().isEmpty());
        assertFalse(newState.findItem(todoItemId).isEmpty());
    }
}
