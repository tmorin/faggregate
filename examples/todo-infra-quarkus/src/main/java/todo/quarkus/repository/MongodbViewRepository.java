package todo.quarkus.repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;
import todo.model.mapper.TodoMappers;
import todo.model.view.TodoItemsView;
import todo.model.view.TodoListView;
import todo.model.view.TodoListViewRepository;
import todo.model.view.TodoListsView;

/**
 * The implementation of the {@link TodoListViewRepository} for MongoDB.
 */
@Slf4j
@ApplicationScoped
public class MongodbViewRepository implements TodoListViewRepository {

    @Inject
    MongodbCommandRepository mongodbCommandRepository;

    @Override
    public CompletionStage<TodoListsView> loadLists() {
        val todoListViews = StreamSupport
            .stream(mongodbCommandRepository.getStateCollection().find().spliterator(), false)
            .map(StateRecord::getBody)
            .map(TodoMappers::toView)
            .collect(Collectors.toList());
        return CompletableFuture.completedFuture(TodoListsView.builder().lists(todoListViews).build());
    }

    @Override
    public CompletionStage<Optional<TodoListView>> loadList(TodoListId todoListId) {
        return mongodbCommandRepository
            .load(todoListId)
            .thenApply(todoList -> todoList.map(StateRecord::getBody).map(TodoMappers::toView));
    }

    @Override
    public CompletionStage<Optional<TodoItemsView>> loadItems(TodoListId todoListId) {
        return mongodbCommandRepository
            .load(todoListId)
            .thenApply(oTodoList ->
                oTodoList
                    .map(StateRecord::getBody)
                    .map(todoList -> {
                        val items = todoList
                            .items()
                            .stream()
                            .map(todoItem -> TodoMappers.toView(todoList, todoItem))
                            .collect(Collectors.toList());
                        return TodoItemsView.builder().todoListId(todoListId).items(items).build();
                    })
            );
    }
}
