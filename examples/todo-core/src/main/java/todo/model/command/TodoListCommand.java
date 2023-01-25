package todo.model.command;

import todo.model.TodoListId;

/**
 * Represent a command of the TodoList aggregate.
 */
public interface TodoListCommand {
    /**
     * The identifier of the aggregate.
     *
     * @return the identifier
     */
    TodoListId getTodoListId();
}
