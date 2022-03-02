package todo.model.command;

import io.morin.faggregate.api.AggregateManagerBuilder;
import io.morin.faggregate.api.Configurer;
import javax.enterprise.context.ApplicationScoped;
import lombok.ToString;
import todo.model.TodoListId;
import todo.model.event.TodoItemAddedEvt;
import todo.model.event.TodoItemRemovedEvt;
import todo.model.event.TodoItemToggledEvt;
import todo.model.event.TodoListCreatedEvt;

/**
 * The factory creates {@link TodoList} aggregates.
 */
@ApplicationScoped
@TodoListConfigurer
@ToString
public class TodoListModelConfigurer implements Configurer<TodoListId, TodoList> {

    /**
     * Create a {@link TodoList} aggregate.
     */
    public void configure(AggregateManagerBuilder<TodoListId, TodoList> builder) {
        // INITIALIZER
        builder.set(new TodoListInitializer());

        // HANDLERS
        builder.add(AddTodoItemCmd.class, new AddTodoItemHandler());
        builder.add(CreateTodoListCmd.class, new CreateTodoListHandler());
        builder.add(RemoveTodoItemCmd.class, new RemoveTodoItemHandler());
        builder.add(ToggleTodoItemCmd.class, new ToggleTodoItemHandler());

        // MUTATORS
        builder.add(TodoItemAddedEvt.class, new TodoItemAddedMutator());
        builder.add(TodoListCreatedEvt.class, new TodoListCreatedMutator());
        builder.add(TodoItemRemovedEvt.class, new TodoItemRemovedMutator());
        builder.add(TodoItemToggledEvt.class, new TodoItemToggledMutator());
    }
}
