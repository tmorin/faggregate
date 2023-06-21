package todo.model.query;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.CompletionStage;
import todo.model.view.TodoListViewRepository;
import todo.model.view.TodoListsView;

/**
 * Handle the {@link ListTodoListsQry} query.
 */
@ApplicationScoped
@QueryHandler.Handle(ListTodoListsQry.class)
class ListTodoListsHandler implements QueryHandler<ListTodoListsQry, TodoListsView> {

    @Inject
    TodoListViewRepository repository;

    public CompletionStage<TodoListsView> execute(ListTodoListsQry query) {
        return repository.loadLists();
    }
}
