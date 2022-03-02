package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.event.TodoItemAddedEvt;
import todo.model.event.TodoItemToggledEvt;

class AddTodoItemHandlerTest {

    String label = "a label";
    TodoItemId todoItemId = TodoItemId.generate();
    TodoListId todoListId = TodoListId.generate();

    @Test
    @SneakyThrows
    void shouldHandlerCompleted() {
        val state = ImmutableTodoList.builder().todoListId(todoListId).build();
        val command = new AddTodoItemCmd(todoListId, todoItemId, label);
        val handler = new AddTodoItemHandler();
        val output = handler.handle(state, command).get();
        assertFalse(output.getResult().isEmpty());
        assertEquals(output.getResult().get().getLabel(), label);
        assertFalse(output.getEvents().isEmpty());
        assertEquals(new TodoItemAddedEvt(todoListId, todoItemId, label), output.getEvents().get(0));
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
