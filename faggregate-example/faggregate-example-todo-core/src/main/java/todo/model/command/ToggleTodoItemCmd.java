package todo.model.command;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoItemId;
import todo.model.TodoListId;

/**
 * The command triggers the toggling of an item.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class ToggleTodoItemCmd implements TodoListCommand {

    /**
     * The identifier of the TodoList.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The identifier of the item.
     */
    @NonNull
    TodoItemId todoItemId;
}
