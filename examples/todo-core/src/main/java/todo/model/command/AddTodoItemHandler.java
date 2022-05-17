package todo.model.command;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;
import todo.model.event.TodoItemAddedEvt;
import todo.model.view.TodoItemView;

/**
 * Handle the {@link AddTodoItemCmd} command.
 */
class AddTodoItemHandler implements Handler<TodoList, AddTodoItemCmd, TodoItemView> {

    @Override
    public CompletableFuture<Output<TodoItemView>> handle(TodoList state, AddTodoItemCmd command) {
        val event = TodoItemAddedEvt
            .builder()
            .todoListId(command.getTodoListId())
            .todoItemId(command.getTodoItemId())
            .label(command.getLabel())
            .build();
        val view = TodoItemView
            .builder()
            .todoListId(event.getTodoListId())
            .todoItemId(event.getTodoItemId())
            .label(event.getLabel())
            .build();
        val output = OutputBuilder.get(view).add(event).build();
        return CompletableFuture.completedFuture(output);
    }
}
