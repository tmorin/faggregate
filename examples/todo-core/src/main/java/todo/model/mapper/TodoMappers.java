package todo.model.mapper;

import todo.model.command.TodoItem;
import todo.model.command.TodoList;
import todo.model.view.TodoItemView;
import todo.model.view.TodoListView;

/**
 * Map entities to views.
 */
public class TodoMappers {

    /**
     * Map a {@link TodoList}.
     *
     * @param todoList the list entity
     * @return the view
     */
    public static TodoListView toView(TodoList todoList) {
        return TodoListView.builder().todoListId(todoList.todoListId()).label(todoList.label()).build();
    }

    /**
     * Map a {@link TodoItem}.
     *
     * @param todoList the list entity
     * @param todoItem the item entity
     * @return the view
     */
    public static TodoItemView toView(TodoList todoList, TodoItem todoItem) {
        return TodoItemView
            .builder()
            .todoListId(todoList.todoListId())
            .todoItemId(todoItem.todoItemId())
            .label(todoItem.label())
            .build();
    }
}
