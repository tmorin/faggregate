package faggregate.tutorial;

import io.morin.faggregate.api.*;
import lombok.RequiredArgsConstructor;

/**
 * The configurer binds the handlers, mutator as well as side effects.
 */
@RequiredArgsConstructor
class CounterConfigurer implements Configurer<String, Counter> {

    private final CounterRepository repository;

    @Override
    public void configure(AggregateManagerBuilder<String, Counter> builder) {
        // set the initializer
        builder.set((Initializer<String, Counter>) repository);
        // set the loader
        builder.set((Loader<String, Counter>) repository);
        // set the persister
        builder.set((Persister<String, Counter>) repository);
        // set the destroyer
        builder.set((Destroyer<String, Counter>) repository);
        // add the command handler
        builder.add(IncrementCounter.class, new IncrementCounterHandler());
        // add the mutator
        builder.add(CounterChanged.class, new CounterChangedMutator());
    }
}
