package todo.model.command;

import io.morin.faggregate.api.Mutator;
import todo.model.event.TodoListCreatedEvt;

/**
 * A mutator of the {@link TodoListCreatedEvt} event.
 */
class TodoListCreatedMutator implements Mutator<TodoList, TodoListCreatedEvt> {

    @Override
    public TodoList mutate(TodoList state, TodoListCreatedEvt event) {
        return ImmutableTodoList.copyOf(state).withTodoListId(event.getTodoListId()).withLabel(event.getLabel());
    }
}
