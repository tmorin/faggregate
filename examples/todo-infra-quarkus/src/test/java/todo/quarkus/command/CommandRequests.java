package todo.quarkus.command;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.command.*;
import todo.model.view.TodoItemView;
import todo.model.view.TodoListView;

public class CommandRequests {

    public static TodoListView executeCreateTodoList() {
        return RestAssured
            .given()
            .when()
            .body(new CreateTodoListCmd(TodoListId.generate(), "label"))
            .contentType(ContentType.JSON)
            .post("/command/{name}", CreateTodoListCmd.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoListView.class);
    }

    public static TodoListView executeDeleteTodoList(TodoListId todoListId) {
        return defineDeleteTodoList(todoListId).statusCode(200).extract().body().as(TodoListView.class);
    }

    public static ValidatableResponse defineDeleteTodoList(TodoListId todoListId) {
        return RestAssured
            .given()
            .when()
            .body(new DeleteTodoListCmd(todoListId))
            .contentType(ContentType.JSON)
            .post("/command/{name}", DeleteTodoListCmd.class.getName())
            .then();
    }

    public static TodoItemView validateAddTodoItem(TodoListId todoListId) {
        return RestAssured
            .given()
            .when()
            .body(new AddTodoItemCmd(todoListId, TodoItemId.generate(), "label"))
            .contentType(ContentType.JSON)
            .post("/command/{name}", AddTodoItemCmd.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemView.class);
    }

    public static TodoItemView executeToggleTodoItem(TodoListId todoListId, TodoItemId todoItemId) {
        return RestAssured
            .given()
            .when()
            .body(new ToggleTodoItemCmd(todoListId, todoItemId))
            .contentType(ContentType.JSON)
            .post("/command/{name}", ToggleTodoItemCmd.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemView.class);
    }

    public static TodoItemView executeRemoveTodoItem(TodoListId todoListId, TodoItemId todoItemId) {
        return RestAssured
            .given()
            .when()
            .body(new RemoveTodoItemCmd(todoListId, todoItemId))
            .contentType(ContentType.JSON)
            .post("/command/{name}", RemoveTodoItemCmd.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemView.class);
    }
}
