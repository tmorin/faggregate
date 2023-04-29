package faggregate.tutorial;

import io.morin.faggregate.api.Handler;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.api.OutputBuilder;
import java.util.concurrent.CompletableFuture;
import lombok.val;

/**
 * The {@link IncrementCounterHandler} Command Handler handles the {@link IncrementCounter} Command and produces
 * the {@link CounterChanged} Domain Event based on the latest known state of the {@link Counter} Aggregate.
 */
class IncrementCounterHandler implements Handler<Counter, IncrementCounter, Void> {

    /**
     * There is no special business logic leading to the publication of the {@link CounterChanged} domain event.
     * Therefore, the implementation of the handle is straightforward.
     *
     * @param state   the current state of the aggregate
     * @param command the command
     * @return the handling output
     */
    @Override
    public CompletableFuture<Output<Void>> handle(Counter state, IncrementCounter command) {
        val oldValue = state.getValue();
        val newValue = oldValue + 1;
        val counterChanged = new CounterChanged(state.getCounterId(), oldValue, newValue);
        return CompletableFuture.completedFuture(OutputBuilder.get().add(counterChanged).build());
    }
}
