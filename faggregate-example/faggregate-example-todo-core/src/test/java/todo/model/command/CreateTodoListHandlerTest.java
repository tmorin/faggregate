package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.event.TodoListCreatedEvt;

class CreateTodoListHandlerTest {

    String label = "a label";
    TodoListId todoListId = TodoListId.generate();

    @Test
    @SneakyThrows
    void handle() {
        val state = ImmutableTodoList.builder().todoListId(todoListId).build();
        val command = new CreateTodoListCmd(todoListId, label);
        val handler = new CreateTodoListHandler();
        val output = handler.handle(state, command).get();
        assertFalse(output.getResult().isEmpty());
        assertEquals(output.getResult().get().getLabel(), label);
        assertFalse(output.getEvents().isEmpty());
        Assertions.assertEquals(new TodoListCreatedEvt(todoListId, label), output.getEvents().get(0));
    }
}
