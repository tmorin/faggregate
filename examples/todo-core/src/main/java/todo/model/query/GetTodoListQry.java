package todo.model.query;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * Get a TodoList.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class GetTodoListQry {

    /**
     * The identifier of the aggregate.
     */
    @NonNull
    TodoListId todoListId;
}
