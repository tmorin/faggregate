package todo.model.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CompletionStage;
import javax.inject.Qualifier;

public interface QueryHandler<Q, R> {
    CompletionStage<R> execute(Q query);

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
    @Qualifier
    @interface Handle {
        Class<?> value();
    }
}
