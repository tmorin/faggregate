package todo.model.command;

import io.morin.faggregate.api.Context;
import io.morin.faggregate.api.Initializer;
import java.util.concurrent.CompletableFuture;
import todo.model.TodoListId;

/**
 * The initializer of the TodoList aggregate.
 */
public class TodoListInitializer implements Initializer<TodoListId, TodoList> {

    @Override
    public CompletableFuture<TodoList> initialize(Context<TodoListId, ?> context) {
        return CompletableFuture.completedFuture(
            ImmutableTodoList.builder().todoListId(context.getIdentifier()).build()
        );
    }
}
