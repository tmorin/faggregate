package todo.quarkus.query;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import todo.model.TodoListId;
import todo.model.query.GetTodoListQry;
import todo.model.query.ListTodoItemsQry;
import todo.model.query.ListTodoListsQry;
import todo.model.view.TodoItemsView;
import todo.model.view.TodoListView;
import todo.model.view.TodoListsView;

public class QueryRequests {

    public static TodoListsView listTodoLists() {
        return RestAssured
            .given()
            .when()
            .body(new ListTodoListsQry())
            .contentType(ContentType.JSON)
            .post("/query/{name}", ListTodoListsQry.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoListsView.class);
    }

    public static TodoListView getTodoList(TodoListId todoListId) {
        return RestAssured
            .given()
            .when()
            .body(new GetTodoListQry(todoListId))
            .contentType(ContentType.JSON)
            .post("/query/{name}", GetTodoListQry.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoListView.class);
    }

    public static TodoItemsView listTodoItems(TodoListId todoListId) {
        return RestAssured
            .given()
            .when()
            .body(new ListTodoItemsQry(todoListId))
            .contentType(ContentType.JSON)
            .post("/query/{name}", ListTodoItemsQry.class.getName())
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TodoItemsView.class);
    }
}
