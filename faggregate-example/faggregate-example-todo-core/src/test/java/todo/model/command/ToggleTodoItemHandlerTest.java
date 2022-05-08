package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.event.TodoItemToggledEvt;

class ToggleTodoItemHandlerTest {

    final String label = "a label";
    final TodoItemId todoItemId = TodoItemId.generate();
    final TodoListId todoListId = TodoListId.generate();

    @Test
    @SneakyThrows
    void shouldHandlerCompleted() {
        val todoItem = ImmutableTodoItem.builder().todoItemId(todoItemId).label(label).build();
        val state = ImmutableTodoList.builder().todoListId(todoListId).addItems(todoItem).build();
        val command = new ToggleTodoItemCmd(todoListId, todoItemId);
        val handler = new ToggleTodoItemHandler();
        val output = handler.handle(state, command).get();
        assertFalse(output.getResult().isEmpty());
        assertEquals(output.getResult().get().getLabel(), label);
        assertFalse(output.getEvents().isEmpty());
        Assertions.assertEquals(new TodoItemToggledEvt(todoListId, todoItemId, true), output.getEvents().get(0));
    }

    @Test
    @SneakyThrows
    void shouldHandlerNotCompleted() {
        val todoItem = ImmutableTodoItem.builder().todoItemId(todoItemId).label(label).isCompleted(true).build();
        val state = ImmutableTodoList.builder().todoListId(todoListId).addItems(todoItem).build();
        val command = new ToggleTodoItemCmd(todoListId, todoItemId);
        val handler = new ToggleTodoItemHandler();
        val output = handler.handle(state, command).get();
        assertFalse(output.getResult().isEmpty());
        assertEquals(output.getResult().get().getLabel(), label);
        assertFalse(output.getEvents().isEmpty());
        assertEquals(new TodoItemToggledEvt(todoListId, todoItemId, false), output.getEvents().get(0));
    }
}
