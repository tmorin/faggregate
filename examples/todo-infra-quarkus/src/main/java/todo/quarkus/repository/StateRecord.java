package todo.quarkus.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import todo.model.command.TodoList;

/**
 * The MongoDB representation of {@link TodoList}.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
class StateRecord {

    /**
     * The version of the state.
     */
    int version;

    /**
     * <code>true</code> when the aggregate has been destroyed.
     */
    boolean deleted;

    /**
     * The state of the aggregate.
     */
    TodoList body;
}
