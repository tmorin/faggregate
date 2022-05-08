package todo.model.command;

import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;
import todo.model.TodoItemId;
import todo.model.TodoListId;

/**
 * The root aggregate.
 */
@Value.Immutable
public interface TodoList {
    /**
     * The identifier of the TodoList.
     */
    TodoListId todoListId();

    /**
     * The label of the TodoList.
     */
    @Value.Default
    default String label() {
        return "";
    }

    /**
     * The list of items.
     */
    List<TodoItem> items();

    /**
     * Find an item from its identifier.
     *
     * @param todoItemId the identifier
     * @return the item when found
     */
    default Optional<TodoItem> findItem(TodoItemId todoItemId) {
        return items().stream().filter(i -> i.todoItemId().equals(todoItemId)).findFirst();
    }
}
