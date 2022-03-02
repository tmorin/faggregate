package todo.model.query;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import todo.model.view.TodoListView;

@ApplicationScoped
@QueryHandler.Handle(GetTodoListQry.class)
public class GetTodoListHandler implements QueryHandler<GetTodoListQry, Optional<TodoListView>> {

    @Inject
    TodoListQueryRepository repository;

    public CompletionStage<Optional<TodoListView>> execute(GetTodoListQry query) {
        return repository.loadList(query.getTodoListId());
    }
}
