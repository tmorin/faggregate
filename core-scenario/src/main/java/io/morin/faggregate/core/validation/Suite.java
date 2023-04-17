package io.morin.faggregate.core.validation;

import io.morin.faggregate.api.AggregateManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.*;

/**
 * A suite is a set of scenario designed for the same {@link AggregateManager}.
 */
@Value
@Builder
public class Suite {

    /**
     * The set of scenarios.
     */
    @Singular
    List<Scenario> scenarios;

    /**
     * Execute the scenarios sequentially.
     *
     * @param am     the Aggregate Manager
     * @param before the before lambda
     * @param after  the after lambda
     */
    @SneakyThrows
    public CompletableFuture<Void> execute(
        @NonNull AggregateManager<Object> am,
        @NonNull ScenarioExecutor.Before before,
        @NonNull ScenarioExecutor.After after
    ) {
        val executors = scenarios
            .stream()
            .map(scenario -> ScenarioExecutor.builder().scenario(scenario).am(am).before(before).after(after).build())
            .collect(Collectors.toList());

        for (val executor : executors) {
            executor.execute().toCompletableFuture().get();
        }

        return CompletableFuture.<Void>completedStage(null).toCompletableFuture();
    }
}
