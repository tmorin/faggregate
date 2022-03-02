package todo.model.query;

import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import todo.model.view.TodoListsView;

@ApplicationScoped
@QueryHandler.Handle(ListTodoListsQry.class)
public class ListTodoListsHandler implements QueryHandler<ListTodoListsQry, TodoListsView> {

    @Inject
    TodoListQueryRepository repository;

    public CompletionStage<TodoListsView> execute(ListTodoListsQry query) {
        return repository.loadLists();
    }
}
