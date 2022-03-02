package todo.model.mapper;

import todo.model.command.TodoItem;
import todo.model.command.TodoList;
import todo.model.view.TodoItemView;
import todo.model.view.TodoListView;

public class TodoMappers {

    public static TodoListView toView(TodoList todoList) {
        return TodoListView.builder().todoListId(todoList.todoListId()).label(todoList.label()).build();
    }

    public static TodoItemView toView(TodoList todoList, TodoItem todoItem) {
        return TodoItemView
            .builder()
            .todoListId(todoList.todoListId())
            .todoItemId(todoItem.todoItemId())
            .label(todoItem.label())
            .build();
    }
}
