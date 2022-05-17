package todo.quarkus.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import todo.model.command.TodoList;

@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Jacksonized
public class StateRecord {

    int version;

    boolean deleted;

    TodoList body;
}
