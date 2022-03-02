package todo.model.command;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoItemId;
import todo.model.TodoListId;

/**
 * The command triggers the removal of a TodoItem in a TodoList.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class RemoveTodoItemCmd implements TodoListCommand {

    /**
     * The identifier of the aggregate.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The identifier of the item.
     */
    @NonNull
    TodoItemId todoItemId;
}
