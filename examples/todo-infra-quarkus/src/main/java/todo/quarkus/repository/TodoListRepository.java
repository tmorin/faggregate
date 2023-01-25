package todo.quarkus.repository;

import io.morin.faggregate.api.Destroyer;
import io.morin.faggregate.api.Loader;
import io.morin.faggregate.api.Persister;
import todo.model.TodoListId;
import todo.model.command.TodoList;

/**
 * The repository of {@link TodoList} aggregate.
 * <p>
 * It bundles the loader, the persister and the destroyer.
 */
public interface TodoListRepository
    extends Loader<TodoListId, TodoList>, Persister<TodoListId, TodoList>, Destroyer<TodoListId, TodoList> {}
