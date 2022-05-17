package todo.model.command;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;
import todo.model.event.TodoItemRemovedEvt;
import todo.model.view.TodoItemView;

/**
 * Handle the {@link RemoveTodoItemCmd} command.
 */
class RemoveTodoItemHandler implements Handler<TodoList, RemoveTodoItemCmd, TodoItemView> {

    @Override
    public CompletableFuture<Output<TodoItemView>> handle(TodoList state, RemoveTodoItemCmd command) {
        return state
            .findItem(command.getTodoItemId())
            .map(CompletableFuture::completedFuture)
            .orElseGet(() ->
                CompletableFuture.failedFuture(new TodoItemNotFound(command.getTodoListId(), command.getTodoItemId()))
            )
            .thenApplyAsync(todoItem -> {
                val event = TodoItemRemovedEvt
                    .builder()
                    .todoListId(state.todoListId())
                    .todoItemId(command.getTodoItemId())
                    .build();
                val view = TodoItemView
                    .builder()
                    .todoListId(state.todoListId())
                    .todoItemId(todoItem.todoItemId())
                    .label(todoItem.label())
                    .build();
                return OutputBuilder.get(view).add(event).build();
            });
    }
}
