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
        val todoListsViewBefore = QueryRequests.executeListTodoLists();
        CommandRequests.executeCreateTodoList();
        CommandRequests.executeCreateTodoList();
        CommandRequests.executeCreateTodoList();
        val todoListsView = QueryRequests.executeListTodoLists();
        val diff = todoListsView.getLists().size() - todoListsViewBefore.getLists().size();
        Assertions.assertEquals(3, diff);
    }

    @Test
    void testGetTodoList() {
        val todoListView = CommandRequests.executeCreateTodoList();
        val todoListViewAlt = QueryRequests.executeGetTodoList(todoListView.getTodoListId());
        Assertions.assertEquals(todoListView, todoListViewAlt);
    }

    @Test
    void testListTodoItems() {
        val todoListView = CommandRequests.executeCreateTodoList();
        val todoItemsViewBefore = QueryRequests.executeListTodoItems(todoListView.getTodoListId());
        CommandRequests.validateAddTodoItem(todoListView.getTodoListId());
        CommandRequests.validateAddTodoItem(todoListView.getTodoListId());
        CommandRequests.validateAddTodoItem(todoListView.getTodoListId());
        val todoItemsView = QueryRequests.executeListTodoItems(todoListView.getTodoListId());
        val diff = todoItemsView.getItems().size() - todoItemsViewBefore.getItems().size();
        Assertions.assertEquals(3, diff);
    }
}
