package todo.model.command;

import io.morin.faggregate.api.Initializer;
import java.util.concurrent.CompletableFuture;

/**
 * The initializer of the TodoList aggregate.
 */
public class TodoListInitializer implements Initializer<TodoList> {

    @Override
    public CompletableFuture<TodoList> initialize() {
        return CompletableFuture.completedFuture(ImmutableTodoList.builder().build());
    }
}
