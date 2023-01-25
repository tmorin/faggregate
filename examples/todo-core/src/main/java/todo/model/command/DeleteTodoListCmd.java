package todo.model.command;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * The command triggers the deletion of an existing TodoList.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class DeleteTodoListCmd implements TodoListCommand {

    /**
     * The identifier of the aggregate.
     */
    @NonNull
    TodoListId todoListId;
}
