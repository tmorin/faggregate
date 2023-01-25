package todo.quarkus.repository;

import io.morin.faggregate.simple.core.ExecutionContext;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import javax.inject.Inject;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.model.TodoItemId;
import todo.model.TodoListId;
import todo.model.command.ImmutableTodoItem;
import todo.model.command.ImmutableTodoList;

@QuarkusTest
class MongodbQueryRepositoryTest {

    @Inject
    MongodbCommandRepository mongodbCommandRepository;

    @Inject
    MongodbViewRepository mongodbQueryRepository;

    String label;
    TodoListId todoListIdA;
    TodoListId todoListIdB;
    TodoItemId todoItemIdA;

    @SneakyThrows
    @BeforeEach
    void BeforeEach() {
        label = "label";
        todoListIdA = TodoListId.generate();
        todoItemIdA = TodoItemId.generate();
        todoListIdB = TodoListId.generate();
    }

    @SneakyThrows
    void createFixture() {
        mongodbCommandRepository
            .persist(
                ExecutionContext.create(todoListIdA, "persist"),
                ImmutableTodoList
                    .builder()
                    .todoListId(todoListIdA)
                    .label(label)
                    .addItems(ImmutableTodoItem.builder().todoItemId(todoItemIdA).label(label).build())
                    .build(),
                Collections.emptyList()
            )
            .get();
        mongodbCommandRepository
            .persist(
                ExecutionContext.create(todoListIdB, "persist"),
                ImmutableTodoList.builder().todoListId(todoListIdB).label(label).build(),
                Collections.emptyList()
            )
            .get();
    }

    @SneakyThrows
    @Test
    void loadLists() {
        val nbrBefore = mongodbQueryRepository.loadLists().toCompletableFuture().get().getLists().size();
        createFixture();
        val nbrAfter = mongodbQueryRepository.loadLists().toCompletableFuture().get().getLists().size();
        Assertions.assertEquals(nbrAfter - nbrBefore, 2);
    }

    @SneakyThrows
    @Test
    void loadList() {
        createFixture();
        val todoListView = mongodbQueryRepository.loadList(todoListIdA).toCompletableFuture().get();
        Assertions.assertTrue(todoListView.isPresent());
        Assertions.assertEquals(label, todoListView.get().getLabel());
    }

    @SneakyThrows
    @Test
    void loadItems() {
        createFixture();
        val todoListView = mongodbQueryRepository.loadItems(todoListIdA).toCompletableFuture().get();
        Assertions.assertTrue(todoListView.isPresent());
        Assertions.assertEquals(1, todoListView.get().getItems().size());
    }
}
