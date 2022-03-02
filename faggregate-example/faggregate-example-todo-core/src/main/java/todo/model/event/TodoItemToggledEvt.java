package todo.model.event;

import io.morin.faggregate.api.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoItemId;
import todo.model.TodoListId;

/**
 * The event relates an item has been toggled.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class TodoItemToggledEvt implements Event {

    /**
     * The identifier of the TodoList.
     */
    TodoListId todoListId;

    /**
     * The identifier of the item.
     */
    TodoItemId todoItemId;

    /**
     * The completion flag.
     */
    boolean completed;
}
