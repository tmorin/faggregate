package todo.quarkus.command;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.command.AddTodoItemCmd;
import todo.model.command.CreateTodoListCmd;
import todo.model.command.RemoveTodoItemCmd;
import todo.model.command.ToggleTodoItemCmd;
import todo.model.view.TodoItemView;
import todo.model.view.TodoListView;

public class CommandRequests {

    public static TodoListView createTodoList() {
        return RestAssured
            .given()
            .when()
            .body(new CreateTodoListCmd(TodoListId.generate(), "label"))
            .contentType(ContentType.JSON)
            .post("/command/{name}", CommandDescriptor.CreateTodoList.name())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoListView.class);
    }

    public static TodoItemView addTodoItem(TodoListId todoListId) {
        return RestAssured
            .given()
            .when()
            .body(new AddTodoItemCmd(todoListId, TodoItemId.generate(), "label"))
            .contentType(ContentType.JSON)
            .post("/command/{name}", CommandDescriptor.AddTodoItem.name())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemView.class);
    }

    public static TodoItemView toggleTodoItem(TodoListId todoListId, TodoItemId todoItemId) {
        return RestAssured
            .given()
            .when()
            .body(new ToggleTodoItemCmd(todoListId, todoItemId))
            .contentType(ContentType.JSON)
            .post("/command/{name}", CommandDescriptor.ToggleTodoItem.name())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemView.class);
    }

    public static TodoItemView removeTodoItem(TodoListId todoListId, TodoItemId todoItemId) {
        return RestAssured
            .given()
            .when()
            .body(new RemoveTodoItemCmd(todoListId, todoItemId))
            .contentType(ContentType.JSON)
            .post("/command/{name}", CommandDescriptor.RemoveTodoItem.name())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemView.class);
    }
}
