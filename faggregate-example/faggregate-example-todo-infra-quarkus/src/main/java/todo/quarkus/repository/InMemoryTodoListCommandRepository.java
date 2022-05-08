package todo.quarkus.repository;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import todo.model.TodoListId;
import todo.model.command.TodoList;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@Singleton
public class InMemoryTodoListCommandRepository implements TodoListRepository {

    Map<TodoListId, TodoList> snapshots = new HashMap<>();

    Map<TodoListId, List<Object>> events = new HashMap<>();

    @Override
    public CompletableFuture<Optional<TodoList>> load(TodoListId identifier) {
        return CompletableFuture.completedFuture(Optional.ofNullable(snapshots.get(identifier)));
    }

    @Override
    public <E> CompletableFuture<Void> persist(TodoListId identifier, TodoList state, List<E> events) {
        this.snapshots.put(identifier, state);
        if (!this.events.containsKey(identifier)) {
            this.events.put(identifier, new ArrayList<>());
        }
        this.events.get(identifier).addAll(events);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public <E> CompletableFuture<Void> destroy(TodoListId identifier, TodoList state, List<E> events) {
        this.snapshots.remove(identifier);
        this.events.remove(identifier);
        return CompletableFuture.completedFuture(null);
    }
}
