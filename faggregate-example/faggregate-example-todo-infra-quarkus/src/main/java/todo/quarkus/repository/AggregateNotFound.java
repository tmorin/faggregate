package todo.quarkus.repository;

public class AggregateNotFound extends Exception {

    public AggregateNotFound(Class<?> clazz, String identifier) {
        super(String.format("the aggregate %s/%s cannot be found.", clazz, identifier));
    }
}
