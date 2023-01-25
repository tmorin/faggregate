package todo.model.view;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * The view represents a TodoList.
 */
@Value
@NonFinal
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoListView {

    /**
     * The identifier of the list.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The label.
     */
    @NonNull
    String label;
}
