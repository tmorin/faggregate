package todo.model.command;

import io.morin.faggregate.core.validation.Scenario;
import io.morin.faggregate.core.validation.Suite;
import lombok.experimental.UtilityClass;
import lombok.val;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.event.*;

@UtilityClass
public class TodoSuiteFactory {

    public static Suite create() {
        val todoListId = TodoListId.generate();
        val todoListLabel = "a simple todo list";
        val todoItemIdA = TodoItemId.generate();
        val todoItemLabelA = "item #A";
        return Suite
            .builder()
            .scenario(
                Scenario
                    .builder()
                    .name("Create a new List")
                    .given(Scenario.Given.builder().identifier(todoListId).build())
                    .when(
                        Scenario.When
                            .builder()
                            .command(CreateTodoListCmd.builder().todoListId(todoListId).label(todoListLabel).build())
                            .build()
                    )
                    .then(
                        Scenario.Then
                            .builder()
                            .event(TodoListCreatedEvt.builder().todoListId(todoListId).label(todoListLabel).build())
                            .build()
                    )
                    .build()
            )
            .scenario(
                Scenario
                    .builder()
                    .name("Add a new Item")
                    .given(
                        Scenario.Given
                            .builder()
                            .identifier(todoListId)
                            .event(TodoListCreatedEvt.builder().todoListId(todoListId).label(todoListLabel).build())
                            .build()
                    )
                    .when(
                        Scenario.When
                            .builder()
                            .command(
                                AddTodoItemCmd
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .label(todoItemLabelA)
                                    .build()
                            )
                            .build()
                    )
                    .then(
                        Scenario.Then
                            .builder()
                            .event(
                                TodoItemAddedEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .label(todoItemLabelA)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .scenario(
                Scenario
                    .builder()
                    .name("Toggle an existing Item")
                    .given(
                        Scenario.Given
                            .builder()
                            .identifier(todoListId)
                            .event(TodoListCreatedEvt.builder().todoListId(todoListId).label(todoListLabel).build())
                            .event(
                                TodoItemAddedEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .label(todoListLabel)
                                    .build()
                            )
                            .build()
                    )
                    .when(
                        Scenario.When
                            .builder()
                            .command(ToggleTodoItemCmd.builder().todoListId(todoListId).todoItemId(todoItemIdA).build())
                            .build()
                    )
                    .then(
                        Scenario.Then
                            .builder()
                            .event(
                                TodoItemToggledEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .completed(true)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .scenario(
                Scenario
                    .builder()
                    .name("Toggle an existing Item")
                    .given(
                        Scenario.Given
                            .builder()
                            .identifier(todoListId)
                            .event(TodoListCreatedEvt.builder().todoListId(todoListId).label(todoListLabel).build())
                            .event(
                                TodoItemAddedEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .label(todoListLabel)
                                    .build()
                            )
                            .event(
                                TodoItemToggledEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .completed(true)
                                    .build()
                            )
                            .build()
                    )
                    .when(
                        Scenario.When
                            .builder()
                            .command(ToggleTodoItemCmd.builder().todoListId(todoListId).todoItemId(todoItemIdA).build())
                            .build()
                    )
                    .then(
                        Scenario.Then
                            .builder()
                            .event(
                                TodoItemToggledEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .completed(false)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .scenario(
                Scenario
                    .builder()
                    .name("Remove an existing Item")
                    .given(
                        Scenario.Given
                            .builder()
                            .identifier(todoListId)
                            .event(TodoListCreatedEvt.builder().todoListId(todoListId).label(todoListLabel).build())
                            .event(
                                TodoItemAddedEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .label(todoListLabel)
                                    .build()
                            )
                            .build()
                    )
                    .when(
                        Scenario.When
                            .builder()
                            .command(RemoveTodoItemCmd.builder().todoListId(todoListId).todoItemId(todoItemIdA).build())
                            .build()
                    )
                    .then(
                        Scenario.Then
                            .builder()
                            .event(TodoItemRemovedEvt.builder().todoListId(todoListId).todoItemId(todoItemIdA).build())
                            .build()
                    )
                    .build()
            )
            .scenario(
                Scenario
                    .builder()
                    .name("Delete an existing List")
                    .given(
                        Scenario.Given
                            .builder()
                            .identifier(todoListId)
                            .event(TodoListCreatedEvt.builder().todoListId(todoListId).label(todoListLabel).build())
                            .event(
                                TodoItemAddedEvt
                                    .builder()
                                    .todoListId(todoListId)
                                    .todoItemId(todoItemIdA)
                                    .label(todoListLabel)
                                    .build()
                            )
                            .build()
                    )
                    .when(
                        Scenario.When
                            .builder()
                            .command(DeleteTodoListCmd.builder().todoListId(todoListId).build())
                            .build()
                    )
                    .then(
                        Scenario.Then
                            .builder()
                            .event(TodoListDeletedEvt.builder().todoListId(todoListId).build())
                            .build()
                    )
                    .build()
            )
            .build();
    }
}
