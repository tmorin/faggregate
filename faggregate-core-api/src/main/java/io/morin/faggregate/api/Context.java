package io.morin.faggregate.api;

import java.util.Optional;

/**
 * <p>A context hosts data related to a command handling.
 *
 * <p>It can be accessed from the following artifacts:
 *
 * <ul>
 *     <li>{@link Initializer}</li>
 *     <li>{@link Loader}</li>
 *     <li>{@link Persister}</li>
 *     <li>{@link Destroyer}</li>
 *     <li>{@link Middleware}</li>
 * </ul>
 *
 * @param <I> the identifier type of the aggregate
 * @param <C> the type of the command
 */
public interface Context<I, C> {
    /**
     * The identifier of the aggregate.
     *
     * @return the identifier
     */
    I getIdentifier();

    /**
     * The handled command.
     *
     * @return the command
     */
    C getCommand();

    /**
     * Set a custom attribute.
     *
     * @param name  the name
     * @param value the value
     * @param <K>   the type of the key
     * @param <V>   the type of the value
     * @return the current context
     */
    <K, V> Context<I, C> set(K name, V value);

    /**
     * Get a custom attribute.
     *
     * @param name the name
     * @param <K>  the type of the key
     * @param <V>  the type of the value
     * @return the value
     */
    <K, V> Optional<V> get(K name);
}
