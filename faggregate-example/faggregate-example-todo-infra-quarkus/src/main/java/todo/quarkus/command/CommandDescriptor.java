package todo.quarkus.command;

import todo.model.command.*;

public enum CommandDescriptor {
    CreateTodoList(CreateTodoListCmd.class, true),
    AddTodoItem(AddTodoItemCmd.class),
    RemoveTodoItem(RemoveTodoItemCmd.class),
    ToggleTodoItem(ToggleTodoItemCmd.class);

    public final Class<? extends TodoListCommand> type;
    public final boolean initializer;

    CommandDescriptor(Class<? extends TodoListCommand> type, boolean initializer) {
        this.type = type;
        this.initializer = initializer;
    }

    CommandDescriptor(Class<? extends TodoListCommand> type) {
        this.type = type;
        this.initializer = false;
    }
}
