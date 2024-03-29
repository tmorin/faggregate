package faggregate.tutorial;

import io.morin.faggregate.api.AggregateManagerBuilder;
import io.morin.faggregate.api.Configurer;
import io.morin.faggregate.api.Loader;
import io.morin.faggregate.api.Persister;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The configurer binds the handlers, mutator as well as side effects.
 */
@RequiredArgsConstructor
public class CounterConfigurer implements Configurer<String, Counter> {

    private final CounterRepository repository;

    /**
     * Create a new instance of the configurer.
     *
     * @param repository the repository to use
     * @return the configurer
     */
    public static CounterConfigurer create(@NonNull CounterRepository repository) {
        return new CounterConfigurer(repository);
    }

    @Override
    public void configure(AggregateManagerBuilder<String, Counter> builder) {
        // set the loader
        builder.set((Loader<String, Counter>) repository);
        // set the persister
        builder.set((Persister<String, Counter>) repository);
        // add the command handler
        builder.add(IncrementCounter.class, new IncrementCounterHandler());
        // add the mutator
        builder.add(CounterChanged.class, new CounterChangedMutator());
    }
}
