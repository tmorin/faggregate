package todo.model.query;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * Get the items of TodoList.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class ListTodoItemsQry {

    /**
     * The identifier of the aggregate.
     */
    @NonNull
    TodoListId todoListId;
}
