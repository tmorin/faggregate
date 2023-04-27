package todo.quarkus;

import io.morin.faggregate.api.AggregateManager;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import todo.model.TodoListId;
import todo.model.TodoSuiteFactory;

@QuarkusTest
class SuiteTest {

    @Inject
    AggregateManager<TodoListId> aggregateManager;

    @SneakyThrows
    @Test
    void shouldValidateTest() {
        Assertions.assertNotNull(aggregateManager);
        TodoSuiteFactory.create().execute(aggregateManager).get();
    }
}
