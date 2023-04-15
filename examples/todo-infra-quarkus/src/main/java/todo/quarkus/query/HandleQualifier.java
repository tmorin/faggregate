package todo.quarkus.query;

import javax.enterprise.util.AnnotationLiteral;
import lombok.EqualsAndHashCode;
import todo.model.query.QueryHandler;

/**
 * An implementation of the {@link QueryHandler.Handle} qualifier.
 */
@EqualsAndHashCode(callSuper = true)
class HandleQualifier extends AnnotationLiteral<QueryHandler.Handle> implements QueryHandler.Handle {

    final Class<?> type;

    HandleQualifier(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> value() {
        return type;
    }
}
