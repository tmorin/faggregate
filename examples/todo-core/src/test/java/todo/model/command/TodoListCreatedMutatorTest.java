package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.event.TodoListCreatedEvt;

class TodoListCreatedMutatorTest {

    final String label = "a label";
    final TodoListId todoListId = TodoListId.generate();

    @Test
    void shouldMutate() {
        val state = ImmutableTodoList.builder().todoListId(todoListId).build();
        val TodoListCreatedMutator = new TodoListCreatedMutator();
        val newState = TodoListCreatedMutator.mutate(state, new TodoListCreatedEvt(todoListId, label));
        assertEquals(todoListId, newState.todoListId());
        assertEquals(label, newState.label());
    }
}
