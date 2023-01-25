package todo.quarkus.query;

import javax.enterprise.util.AnnotationLiteral;
import todo.model.query.QueryHandler;

/**
 * An implementation of the {@link QueryHandler.Handle} qualifier.
 */
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
