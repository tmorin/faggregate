package todo.model.command;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;
import todo.model.event.TodoListCreatedEvt;
import todo.model.view.TodoListView;

/**
 * Handle the {@link CreateTodoListCmd} command.
 */
class CreateTodoListHandler implements Handler<TodoList, CreateTodoListCmd, TodoListView> {

    @Override
    public CompletableFuture<Output<TodoListView>> handle(TodoList state, CreateTodoListCmd command) {
        val event = TodoListCreatedEvt.builder().todoListId(command.getTodoListId()).label(command.getLabel()).build();
        val view = TodoListView.builder().todoListId(event.getTodoListId()).label(event.getLabel()).build();
        val output = OutputBuilder.get(view).add(event).build();
        return CompletableFuture.completedFuture(output);
    }
}
