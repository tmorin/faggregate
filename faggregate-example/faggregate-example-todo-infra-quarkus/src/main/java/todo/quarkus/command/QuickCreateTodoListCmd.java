package todo.quarkus.command;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class QuickCreateTodoListCmd {

    /**
     * The label of the TodoList.
     */
    String label;
}
