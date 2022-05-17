package todo.model.command;

import lombok.NonNull;
import todo.model.TodoItemId;
import todo.model.TodoListId;

/**
 * The exception is thrown when an item cannot be found in the TodoList.
 */
public class TodoItemNotFound extends Exception {

    TodoItemNotFound(@NonNull TodoListId todoListId, @NonNull TodoItemId todoItemId) {
        super(String.format("unable to find the Todo Item %s / %s", todoListId, todoItemId));
    }
}
