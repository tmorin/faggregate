package todo.model.query;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import todo.model.view.TodoListView;
import todo.model.view.TodoListViewRepository;

/**
 * Handle the {@link GetTodoListQry} query.
 */
@ApplicationScoped
@QueryHandler.Handle(GetTodoListQry.class)
class GetTodoListHandler implements QueryHandler<GetTodoListQry, Optional<TodoListView>> {

    @Inject
    TodoListViewRepository repository;

    public CompletionStage<Optional<TodoListView>> execute(GetTodoListQry query) {
        return repository.loadList(query.getTodoListId());
    }
}
