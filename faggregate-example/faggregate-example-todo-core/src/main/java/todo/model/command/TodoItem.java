package todo.model.command;

import org.immutables.value.Value;
import todo.model.TodoItemId;

/**
 * An entity which hosts the state of a TodoList item.
 */
@Value.Immutable
public interface TodoItem {
    /**
     * The identifier of the item.
     */
    TodoItemId todoItemId();

    /**
     * The label of the item.
     */
    @Value.Default
    default String label() {
        return "";
    }

    /**
     * The completion flag of the item.
     */
    @Value.Default
    default boolean isCompleted() {
        return false;
    }
}
