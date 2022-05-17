package todo.quarkus.command;

import io.quarkus.test.junit.QuarkusTest;
import lombok.val;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CommandGatewayTest {

    @Test
    void testCreateTodoList() {
        CommandRequests.createTodoList();
    }

    @Test
    void testAddTodoItem() {
        val todoListView = CommandRequests.createTodoList();
        CommandRequests.addTodoItem(todoListView.getTodoListId());
    }

    @Test
    void testToggleTodoItem() {
        val todoListView = CommandRequests.createTodoList();
        val todoItemView = CommandRequests.addTodoItem(todoListView.getTodoListId());
        CommandRequests.toggleTodoItem(todoListView.getTodoListId(), todoItemView.getTodoItemId());
    }

    @Test
    void testRemoveTodoItem() {
        val todoListView = CommandRequests.createTodoList();
        val todoItemView = CommandRequests.addTodoItem(todoListView.getTodoListId());
        CommandRequests.removeTodoItem(todoListView.getTodoListId(), todoItemView.getTodoItemId());
    }
}
