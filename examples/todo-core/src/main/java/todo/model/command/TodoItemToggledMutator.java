package todo.model.command;

import io.morin.faggregate.api.Mutator;
import java.util.ArrayList;
import lombok.val;
import todo.model.event.TodoItemToggledEvt;

/**
 * A mutator of the {@link TodoItemToggledEvt} event.
 */
class TodoItemToggledMutator implements Mutator<TodoList, TodoItemToggledEvt> {

    @Override
    public TodoList mutate(TodoList state, TodoItemToggledEvt event) {
        val newTodoList = ImmutableTodoList.copyOf(state);
        return state
            .findItem(event.getTodoItemId())
            .map(todoItem -> {
                val newItems = new ArrayList<>(newTodoList.items());
                val index = newItems.indexOf(todoItem);
                newItems.remove(todoItem);
                newItems.add(index, ImmutableTodoItem.copyOf(todoItem).withIsCompleted(event.isCompleted()));
                return newTodoList.withItems(newItems);
            })
            .orElse(newTodoList);
    }
}
