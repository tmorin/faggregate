package todo.model.command;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * The command triggers the creation of a fresh new TodoList.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class CreateTodoListCmd implements TodoListCommand {

    /**
     * The identifier of the aggregate.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The label of the TodoList.
     */
    @NonNull
    String label;
}
