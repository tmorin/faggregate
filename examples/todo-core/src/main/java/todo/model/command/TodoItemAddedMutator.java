package todo.model.command;

import io.morin.faggregate.api.Mutator;
import java.util.ArrayList;
import lombok.val;
import todo.model.event.TodoItemAddedEvt;

/**
 * A mutator of the {@link TodoItemAddedEvt} event.
 */
class TodoItemAddedMutator implements Mutator<TodoList, TodoItemAddedEvt> {

    @Override
    public TodoList mutate(TodoList state, TodoItemAddedEvt event) {
        val todoItem = ImmutableTodoItem.builder().todoItemId(event.getTodoItemId()).label(event.getLabel()).build();
        val todoList = ImmutableTodoList.copyOf(state);
        val newItems = new ArrayList<>(todoList.items());
        newItems.add(todoItem);
        return todoList.withItems(newItems);
    }
}
