package todo.quarkus.repository;

import io.morin.faggregate.api.Context;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import todo.model.TodoListId;
import todo.model.command.TodoList;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@Singleton
public class InMemoryTodoListCommandRepository implements TodoListRepository {

    Map<TodoListId, TodoList> snapshots = new HashMap<>();

    Map<TodoListId, List<Object>> events = new HashMap<>();

    @Override
    public CompletableFuture<Optional<TodoList>> load(Context<TodoListId, ?> context) {
        return CompletableFuture.completedFuture(Optional.ofNullable(snapshots.get(context.getIdentifier())));
    }

    @Override
    public <E> CompletableFuture<Void> persist(Context<TodoListId, ?> context, TodoList state, List<E> events) {
        val identifier = context.getIdentifier();
        this.snapshots.put(identifier, state);
        if (!this.events.containsKey(identifier)) {
            this.events.put(identifier, new ArrayList<>());
        }
        this.events.get(identifier).addAll(events);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public <E> CompletableFuture<Void> destroy(Context<TodoListId, ?> context, TodoList state, List<E> events) {
        this.snapshots.remove(context.getIdentifier());
        this.events.remove(context.getIdentifier());
        return CompletableFuture.completedFuture(null);
    }
}
