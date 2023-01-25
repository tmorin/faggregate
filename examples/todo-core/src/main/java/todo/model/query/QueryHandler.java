package todo.model.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CompletionStage;
import javax.inject.Qualifier;

/**
 * A query handler process a query and provide a result.
 *
 * @param <Q> the type of the query
 * @param <R> the type of the result
 */
public interface QueryHandler<Q, R> {
    CompletionStage<R> execute(Q query);

    /**
     * The qualifier is used to map by annotation a query to its handler.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
    @Qualifier
    @interface Handle {
        /**
         * @return The type of the query
         */
        Class<?> value();
    }
}
