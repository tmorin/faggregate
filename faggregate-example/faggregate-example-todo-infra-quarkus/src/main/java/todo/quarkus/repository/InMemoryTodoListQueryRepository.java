package todo.quarkus.repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.val;
import todo.model.TodoListId;
import todo.model.mapper.TodoMappers;
import todo.model.query.TodoListQueryRepository;
import todo.model.view.TodoItemsView;
import todo.model.view.TodoListView;
import todo.model.view.TodoListsView;

@ApplicationScoped
public class InMemoryTodoListQueryRepository implements TodoListQueryRepository {

    @Inject
    InMemoryTodoListCommandRepository commandRepository;

    @Override
    public CompletionStage<TodoListsView> loadLists() {
        val view = TodoListsView
            .builder()
            .lists(commandRepository.snapshots.values().stream().map(TodoMappers::toView).collect(Collectors.toList()))
            .build();
        return CompletableFuture.completedStage(view);
    }

    @Override
    public CompletionStage<Optional<TodoListView>> loadList(TodoListId todoListId) {
        val view = Optional.ofNullable(commandRepository.snapshots.get(todoListId)).map(TodoMappers::toView);
        return CompletableFuture.completedStage(view);
    }

    @Override
    public CompletionStage<Optional<TodoItemsView>> loadItems(TodoListId todoListId) {
        val view = Optional
            .ofNullable(commandRepository.snapshots.get(todoListId))
            .map(todoList -> {
                val items = todoList
                    .items()
                    .stream()
                    .map(todoItem -> TodoMappers.toView(todoList, todoItem))
                    .collect(Collectors.toList());
                return TodoItemsView.builder().todoListId(todoListId).items(items).build();
            });
        return CompletableFuture.completedStage(view);
    }
}
