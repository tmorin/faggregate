package todo.quarkus.command;

import io.quarkus.test.junit.QuarkusTest;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CommandGatewayTest {

    @Test
    void testCreateTodoList() {
        Assertions.assertDoesNotThrow(CommandRequests::executeCreateTodoList);
    }

    @Test
    void testDeleteTodoList() {
        val todoListView = CommandRequests.executeCreateTodoList();
        CommandRequests.executeDeleteTodoList(todoListView.getTodoListId());
        CommandRequests.defineDeleteTodoList(todoListView.getTodoListId()).statusCode(404);
    }

    @Test
    void testAddTodoItem() {
        val todoListView = CommandRequests.executeCreateTodoList();
        CommandRequests.validateAddTodoItem(todoListView.getTodoListId());
    }

    @Test
    void testToggleTodoItem() {
        val todoListView = CommandRequests.executeCreateTodoList();
        val todoItemView = CommandRequests.validateAddTodoItem(todoListView.getTodoListId());
        CommandRequests.executeToggleTodoItem(todoListView.getTodoListId(), todoItemView.getTodoItemId());
    }

    @Test
    void testRemoveTodoItem() {
        val todoListView = CommandRequests.executeCreateTodoList();
        val todoItemView = CommandRequests.validateAddTodoItem(todoListView.getTodoListId());
        CommandRequests.executeRemoveTodoItem(todoListView.getTodoListId(), todoItemView.getTodoItemId());
    }
}
