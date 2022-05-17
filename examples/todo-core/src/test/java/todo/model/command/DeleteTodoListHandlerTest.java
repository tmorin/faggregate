package todo.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.event.TodoListDeletedEvt;

class DeleteTodoListHandlerTest {

    final String label = "a label";
    final TodoListId todoListId = TodoListId.generate();

    @Test
    @SneakyThrows
    void handle() {
        val state = ImmutableTodoList.builder().todoListId(todoListId).label(label).build();
        val command = new DeleteTodoListCmd(todoListId);
        val handler = new DeleteTodoListHandler();
        val output = handler.handle(state, command).get();
        assertFalse(output.getResult().isEmpty());
        assertEquals(output.getResult().get().getLabel(), label);
        assertFalse(output.getEvents().isEmpty());
        Assertions.assertEquals(new TodoListDeletedEvt(todoListId), output.getEvents().get(0));
    }
}
