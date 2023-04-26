package todo.quarkus;

import io.morin.faggregate.api.AggregateManager;
import io.quarkus.test.junit.QuarkusTest;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.command.TodoSuiteFactory;

@QuarkusTest
class SuiteTest {

    @Inject
    AggregateManager<TodoListId> aggregateManager;

    @SneakyThrows
    @Test
    void shouldValidateTest() {
        Assertions.assertNotNull(aggregateManager);
        TodoSuiteFactory
            .create()
            .execute(
                aggregateManager,
                (identifier, state, events) -> CompletableFuture.completedStage(null),
                identifier -> CompletableFuture.completedStage(null)
            )
            .get();
    }
}
