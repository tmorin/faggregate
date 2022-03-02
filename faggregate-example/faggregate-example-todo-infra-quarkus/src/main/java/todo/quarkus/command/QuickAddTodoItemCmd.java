package todo.quarkus.command;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

@Data
@Builder
@Jacksonized
public class QuickAddTodoItemCmd {

    /**
     * The identifier of the list.
     */
    @NonNull
    TodoListId todoListId;

    /**
     * The label of the TodoItem.
     */
    @NonNull
    String label;
}
