package todo.model.command;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;
import todo.model.event.TodoListDeletedEvt;
import todo.model.view.TodoListView;

/**
 * Handle the {@link DeleteTodoListCmd} command.
 */
class DeleteTodoListHandler implements Handler<TodoList, DeleteTodoListCmd, TodoListView> {

    @Override
    public CompletableFuture<Output<TodoListView>> handle(TodoList state, DeleteTodoListCmd command) {
        val event = TodoListDeletedEvt.builder().todoListId(command.getTodoListId()).build();
        val view = TodoListView.builder().todoListId(event.getTodoListId()).label(state.label()).build();
        val output = OutputBuilder.get(view).add(event).build();
        return CompletableFuture.completedFuture(output);
    }
}
