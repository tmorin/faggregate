package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.val;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.event.TodoItemRemovedEvt;

class TodoItemRemovedMutatorTest {

    String label = "a label";
    TodoItemId todoItemId = TodoItemId.generate();
    TodoListId todoListId = TodoListId.generate();

    @Test
    void shouldMutate() {
        val todoItem = ImmutableTodoItem.builder().todoItemId(todoItemId).label(label).isCompleted(true).build();
        val state = ImmutableTodoList.builder().todoListId(todoListId).addItems(todoItem).build();
        val todoItemRemovedMutator = new TodoItemRemovedMutator();
        val newState = todoItemRemovedMutator.mutate(state, new TodoItemRemovedEvt(todoListId, todoItemId));
        assertTrue(newState.items().isEmpty());
        assertTrue(newState.findItem(todoItemId).isEmpty());
    }
}
