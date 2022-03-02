package todo.model.command;

import io.morin.faggregate.api.Mutator;
import java.util.ArrayList;
import lombok.val;
import todo.model.event.TodoItemRemovedEvt;

/**
 * A mutator of the {@link TodoItemRemovedEvt} event.
 */
class TodoItemRemovedMutator implements Mutator<TodoList, TodoItemRemovedEvt> {

    @Override
    public TodoList mutate(TodoList state, TodoItemRemovedEvt event) {
        val newTodoList = ImmutableTodoList.copyOf(state);
        return state
            .findItem(event.getTodoItemId())
            .map(todoItem -> {
                val newItems = new ArrayList<>(newTodoList.items());
                newItems.remove(todoItem);
                return newTodoList.withItems(newItems);
            })
            .orElse(newTodoList);
    }
}
