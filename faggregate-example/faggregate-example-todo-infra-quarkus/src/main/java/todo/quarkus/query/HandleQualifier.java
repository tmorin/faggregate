package todo.quarkus.query;

import javax.enterprise.util.AnnotationLiteral;
import todo.model.query.QueryHandler;

class HandleQualifier extends AnnotationLiteral<QueryHandler.Handle> implements QueryHandler.Handle {

    final Class<?> name;

    HandleQualifier(Class<?> name) {
        this.name = name;
    }

    @Override
    public Class<?> value() {
        return name;
    }
}
