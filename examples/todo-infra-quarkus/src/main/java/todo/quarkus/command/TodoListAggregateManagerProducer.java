package todo.quarkus.command;

import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.Configurer;
import io.morin.faggregate.simple.core.SimpleAggregateManagerBuilder;
import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;
import todo.model.command.TodoList;
import todo.model.command.TodoListConfigurer;

@Slf4j
@ApplicationScoped
public class TodoListAggregateManagerProducer implements Supplier<AggregateManager<TodoListId>> {

    @Inject
    @Any
    @TodoListConfigurer
    Instance<Configurer<TodoListId, TodoList>> configurers;

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
