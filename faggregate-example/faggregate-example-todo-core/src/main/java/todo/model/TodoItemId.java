package todo.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The identifier of an item.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class TodoItemId {

    /**
     * The encapsulated UUID.
     */
    UUID uuid;

    /**
     * Generate an identifier randomly.
     */
    public static TodoItemId generate() {
        return new TodoItemId(UUID.randomUUID());
    }
}
