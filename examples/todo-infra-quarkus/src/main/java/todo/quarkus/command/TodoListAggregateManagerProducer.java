package todo.quarkus.command;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.Configurer;
import io.morin.faggregate.simple.core.SimpleAggregateManagerBuilder;
import java.util.function.Supplier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;
import todo.model.command.TodoList;
import todo.model.command.TodoListConfigurer;

/**
 * The producer produces an {@link AggregateManager} for the {@link TodoList} aggregate.
 */
@Slf4j
@ApplicationScoped
class TodoListAggregateManagerProducer implements Supplier<AggregateManager<TodoListId>> {

    @Inject
    @Any
    @TodoListConfigurer
    Instance<Configurer<TodoListId, TodoList>> configurers;

    /**
     * Produce an aggregate manager for the {@link TodoList} aggregate.
     *
     * @return the aggregate manager
     */
    @Override
    @Produces
    public AggregateManager<TodoListId> get() {
        val builder = SimpleAggregateManagerBuilder.<TodoListId, TodoList>get();
        configurers
            .stream()
            .forEach(configurer -> {
                log.info("configure TodoListAggregateManager with {}", configurer);
                configurer.configure(builder);
            });
        return builder.build();
    }
}
