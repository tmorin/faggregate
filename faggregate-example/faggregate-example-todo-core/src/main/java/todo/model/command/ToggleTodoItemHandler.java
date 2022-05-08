package todo.model.command;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;
import todo.model.event.TodoItemToggledEvt;
import todo.model.view.TodoItemView;

/**
 * The handler of the {@link ToggleTodoItemCmd} command.
 */
class ToggleTodoItemHandler implements Handler<TodoList, ToggleTodoItemCmd, TodoItemView> {

    @Override
    public CompletableFuture<Output<TodoItemView>> handle(TodoList state, ToggleTodoItemCmd command) {
        return state
            .findItem(command.getTodoItemId())
            .map(CompletableFuture::completedFuture)
            .orElseGet(() ->
                CompletableFuture.failedFuture(new TodoItemNotFound(command.getTodoListId(), command.getTodoItemId()))
            )
            .thenApplyAsync(todoItem -> {
                val event = TodoItemToggledEvt
                    .builder()
                    .todoListId(state.todoListId())
                    .todoItemId(command.getTodoItemId())
                    .completed(!todoItem.isCompleted())
                    .build();
                val view = TodoItemView
                    .builder()
                    .todoListId(event.getTodoListId())
                    .todoItemId(event.getTodoItemId())
                    .label(todoItem.label())
                    .completed(event.isCompleted())
                    .build();
                return OutputBuilder.get(view).add(event).build();
            });
    }
}
