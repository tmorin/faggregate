package todo.model.mapper;

import lombok.experimental.UtilityClass;
import todo.model.command.TodoItem;
import todo.model.command.TodoList;
import todo.model.view.TodoItemView;
import todo.model.view.TodoListView;

/**
 * Map entities to views.
 */
@UtilityClass
public class TodoMappers {

    /**
     * Map a {@link TodoList}.
     *
     * @param todoList the list entity
     * @return the view
     */
    public TodoListView toView(TodoList todoList) {
        return TodoListView.builder().todoListId(todoList.todoListId()).label(todoList.label()).build();
    }

    /**
     * Map a {@link TodoItem}.
     *
     * @param todoList the list entity
     * @param todoItem the item entity
     * @return the view
     */
    public TodoItemView toView(TodoList todoList, TodoItem todoItem) {
        return TodoItemView
            .builder()
            .todoListId(todoList.todoListId())
            .todoItemId(todoItem.todoItemId())
            .label(todoItem.label())
            .build();
    }
}
