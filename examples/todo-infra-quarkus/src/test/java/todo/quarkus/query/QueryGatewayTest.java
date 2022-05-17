package todo.quarkus.query;

import io.quarkus.test.junit.QuarkusTest;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.quarkus.command.CommandRequests;

@QuarkusTest
class QueryGatewayTest {

    @Test
    void testListTodoLists() {
        val todoListsViewBefore = QueryRequests.listTodoLists();
        CommandRequests.createTodoList();
        CommandRequests.createTodoList();
        CommandRequests.createTodoList();
        val todoListsView = QueryRequests.listTodoLists();
        val diff = todoListsView.getLists().size() - todoListsViewBefore.getLists().size();
        Assertions.assertEquals(3, diff);
    }

    @Test
    void testGetTodoList() {
        val todoListView = CommandRequests.createTodoList();
        val todoListViewAlt = QueryRequests.getTodoList(todoListView.getTodoListId());
        Assertions.assertEquals(todoListView, todoListViewAlt);
    }

    @Test
    void testListTodoItems() {
        val todoListView = CommandRequests.createTodoList();
        val todoItemsViewBefore = QueryRequests.listTodoItems(todoListView.getTodoListId());
        CommandRequests.addTodoItem(todoListView.getTodoListId());
        CommandRequests.addTodoItem(todoListView.getTodoListId());
        CommandRequests.addTodoItem(todoListView.getTodoListId());
        val todoItemsView = QueryRequests.listTodoItems(todoListView.getTodoListId());
        val diff = todoItemsView.getItems().size() - todoItemsViewBefore.getItems().size();
        Assertions.assertEquals(3, diff);
    }
}
