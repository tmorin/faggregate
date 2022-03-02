package todo.quarkus.repository;

import io.morin.faggregate.api.*;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.ToString;
import lombok.val;
import todo.model.TodoListId;
import todo.model.command.TodoList;
import todo.model.command.TodoListConfigurer;

@Singleton
@TodoListConfigurer
@ToString
public class TodoListRepoConfigurer implements Configurer<TodoListId, TodoList> {

    @Inject
    TodoListRepository repository;

    public void configure(AggregateManagerBuilder<TodoListId, TodoList> builder) {
        builder.set((Loader<TodoListId, TodoList>) repository);
        builder.set((Persister<TodoListId, TodoList>) repository);
        builder.set((Destroyer<TodoListId, TodoList>) repository);
    }
}
