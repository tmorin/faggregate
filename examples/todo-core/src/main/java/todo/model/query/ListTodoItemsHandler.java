package todo.model.query;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import todo.model.view.TodoItemsView;
import todo.model.view.TodoListViewRepository;

/**
 * Handle the {@link ListTodoItemsQry} query.
 */
@ApplicationScoped
@QueryHandler.Handle(ListTodoItemsQry.class)
class ListTodoItemsHandler implements QueryHandler<ListTodoItemsQry, Optional<TodoItemsView>> {

    @Inject
    TodoListViewRepository repository;

    public CompletionStage<Optional<TodoItemsView>> execute(ListTodoItemsQry query) {
        return repository.loadItems(query.getTodoListId());
    }
}
