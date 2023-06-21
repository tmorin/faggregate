package todo.model.command;

import io.morin.faggregate.api.AggregateManagerBuilder;
import io.morin.faggregate.api.Configurer;
import io.morin.faggregate.api.Intention;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.ToString;
import todo.model.TodoListId;
import todo.model.event.TodoItemAddedEvt;
import todo.model.event.TodoItemRemovedEvt;
import todo.model.event.TodoItemToggledEvt;
import todo.model.event.TodoListCreatedEvt;

/**
 * Configure the model part of the aggregate manager for {@link TodoList}.
 *
 * <ul>
 *     <li>an initializer</li>
 *     <li>handlers</li>
 *     <li>mutators</li>
 * </ul>
 */
@ApplicationScoped
@TodoListConfigurer
@ToString
public class TodoListModelConfigurer implements Configurer<TodoListId, TodoList> {

    /**
     * Configure the aggregate manager for {@link TodoList}.
     *
     * @param builder the builder.
     */
    public void configure(AggregateManagerBuilder<TodoListId, TodoList> builder) {
        // INITIALIZER
        builder.set(new TodoListInitializer());

        // HANDLERS
        builder.add(AddTodoItemCmd.class, new AddTodoItemHandler());
        builder.add(CreateTodoListCmd.class, new CreateTodoListHandler(), Intention.INITIALIZATION);
        builder.add(RemoveTodoItemCmd.class, new RemoveTodoItemHandler());
        builder.add(ToggleTodoItemCmd.class, new ToggleTodoItemHandler());
        builder.add(DeleteTodoListCmd.class, new DeleteTodoListHandler(), Intention.DESTRUCTION);

        // MUTATORS
        builder.add(TodoItemAddedEvt.class, new TodoItemAddedMutator());
        builder.add(TodoListCreatedEvt.class, new TodoListCreatedMutator());
        builder.add(TodoItemRemovedEvt.class, new TodoItemRemovedMutator());
        builder.add(TodoItemToggledEvt.class, new TodoItemToggledMutator());
    }
}
