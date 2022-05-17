package todo.model.query;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import todo.model.view.TodoItemsView;

@ApplicationScoped
@QueryHandler.Handle(ListTodoItemsQry.class)
public class ListTodoItemsHandler implements QueryHandler<ListTodoItemsQry, Optional<TodoItemsView>> {

    @Inject
    TodoListQueryRepository repository;

    public CompletionStage<Optional<TodoItemsView>> execute(ListTodoItemsQry query) {
        return repository.loadItems(query.getTodoListId());
    }
}
