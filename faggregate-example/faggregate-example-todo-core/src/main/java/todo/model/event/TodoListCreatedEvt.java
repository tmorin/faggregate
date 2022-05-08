package todo.model.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * The event relates a TodoList has been created.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class TodoListCreatedEvt {

    /**
     * The identifier of the TodoList
     */
    TodoListId todoListId;

    /**
     * The label.
     */
    String label;
}
