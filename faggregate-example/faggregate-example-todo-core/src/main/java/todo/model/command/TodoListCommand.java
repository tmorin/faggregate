package todo.model.command;

import io.morin.faggregate.api.Command;
import todo.model.TodoListId;

public interface TodoListCommand extends Command {
    TodoListId getTodoListId();
}
