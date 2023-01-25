package todo.quarkus.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

/**
 * The MongoDB representation of a domain event.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
class EventRecord {

    /**
     * The identifier of the TodoList.
     */
    TodoListId TodoList;

    /**
     *
     * The class of the event.
     */
    String javaType;

    /**
     * The JSON representation of the event.
     */
    Object event;
}
