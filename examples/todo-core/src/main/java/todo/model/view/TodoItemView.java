package todo.model.view;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoItemId;
import todo.model.TodoListId;

/**
 * The view represents the item of a TodoList.
 */
@Value
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoItemView {

    /**
     * The identifier of the list.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The identifier of the item.
     */
    @NonNull
    TodoItemId todoItemId;

    /**
     * The label of the item.
     */
    @NonNull
    String label;

    /**
     * The completion flag of the item.
     */
    boolean completed;
}
