package todo.quarkus.repository;

import io.morin.faggregate.api.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.ToString;
import todo.model.TodoListId;
import todo.model.command.TodoList;
import todo.model.command.TodoListConfigurer;

/**
 * Configure the infra part of the aggregate manager for {@link TodoList}.
 *
 * <ul>
 *     <li>loader</li>
 *     <li>persister</li>
 *     <li>destroyer</li>
 * </ul>
 */
@Singleton
@TodoListConfigurer
@ToString
public class TodoListInfraConfigurer implements Configurer<TodoListId, TodoList> {

    @Inject
    TodoListRepository repository;

    /**
     * Configure the aggregate manager for {@link TodoList}.
     *
     * @param builder the builder.
     */
    public void configure(AggregateManagerBuilder<TodoListId, TodoList> builder) {
        builder.set((Loader<TodoListId, TodoList>) repository);
        builder.set((Persister<TodoListId, TodoList>) repository);
        builder.set((Destroyer<TodoListId, TodoList>) repository);
    }
}
