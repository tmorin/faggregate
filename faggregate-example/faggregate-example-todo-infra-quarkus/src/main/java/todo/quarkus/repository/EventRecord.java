package todo.quarkus.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import todo.model.TodoListId;

@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class EventRecord {

    TodoListId TodoList;

    String javaType;

    Object event;
}
