package todo.model.query;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import todo.model.TodoListId;
import todo.model.view.TodoItemsView;
import todo.model.view.TodoListView;
import todo.model.view.TodoListsView;

public interface TodoListQueryRepository {
    CompletionStage<TodoListsView> loadLists();

    CompletionStage<Optional<TodoListView>> loadList(TodoListId todoListId);

    CompletionStage<Optional<TodoItemsView>> loadItems(TodoListId todoListId);
}
