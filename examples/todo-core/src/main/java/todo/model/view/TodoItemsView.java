package todo.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * The view represents a list of {@link TodoItemView}.
 */
@Value
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoItemsView {

    /**
     * The identifier of the list.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The items for the list.
     */
    @NonNull
    List<TodoItemView> items;

    /**
     * The number of completed items.
     */
    @JsonIgnore
    public long getCompletedCounter() {
        return items.stream().filter(TodoItemView::isCompleted).count();
    }

    /**
     * The number of uncompleted items.
     */
    @JsonIgnore
    public long getUncompletedCounter() {
        return items.size() - getCompletedCounter();
    }
}
