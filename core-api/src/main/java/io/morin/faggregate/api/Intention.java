package io.morin.faggregate.api;

/**
 * <p>An intention drives the action flow of a command execution.
 */
public enum Intention {
    /**
     * This intention leads to the following action flow:
     * <ol>
     * <li>call the {@link Initializer}</li>
     * <li>call the {@link Handler}</li>
     * <li>call the {@link Mutator}</li>
     * <li>call the {@link Persister}</li>
     * </ol>
     */
    INITIALIZATION,
    /**
     * This intention leads to the following action flow:
     * <ol>
     * <li>call the {@link Loader}</li>
     * <li>call the {@link Handler}</li>
     * <li>call the {@link Mutator}</li>
     * <li>call the {@link Persister}</li>
     * </ol>
     */
    MUTATION,
    /**
     * This intention leads to the following action flow:
     * <ol>
     * <li>call the {@link Loader}</li>
     * <li>call the {@link Handler}</li>
     * <li>call the {@link Mutator}</li>
     * <li>call the {@link Destroyer}</li>
     * </ol>
     */
    DESTRUCTION
}
