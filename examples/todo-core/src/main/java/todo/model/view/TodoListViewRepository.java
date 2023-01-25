package todo.model.view;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import todo.model.TodoListId;

/**
 * The repository of the views.
 */
public interface TodoListViewRepository {
    /**
     * Load all lists in one-shot.
     *
     * @return the lists
     */
    CompletionStage<TodoListsView> loadLists();

    /**
     * Load a list from its identifier.
     *
     * @param todoListId the identifier
     * @return the list
     */
    CompletionStage<Optional<TodoListView>> loadList(TodoListId todoListId);

    /**
     * Load the items of a list from its identifier.
     *
     * @param todoListId the identifier of the todo list
     * @return the items
     */
    CompletionStage<Optional<TodoItemsView>> loadItems(TodoListId todoListId);
}
