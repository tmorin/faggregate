package todo.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The identifier of a TodoList.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class TodoListId {

    /**
     * The encapsulated UUID.
     */
    UUID uuid;

    /**
     * Generate an identifier randomly.
     */
    public static TodoListId generate() {
        return new TodoListId(UUID.randomUUID());
    }
}
