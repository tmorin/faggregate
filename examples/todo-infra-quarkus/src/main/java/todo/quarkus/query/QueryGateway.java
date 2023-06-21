package todo.quarkus.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.query.QueryHandler;

/**
 * The gateway process the queries.
 */
@Slf4j
@Path("/query")
public class QueryGateway {

    @Inject
    @Any
    Instance<QueryHandler<?, ?>> queryHandlers;

    @Inject
    ObjectMapper objectMapper;

    private <V> Response toResponse(V view) {
        if (view instanceof Optional) {
            val optionalView = (Optional<?>) view;
            return optionalView.map(body -> Response.ok(body).build()).orElseGet(() -> Response.noContent().build());
        }
        return toResponse(Optional.ofNullable(view));
    }

    @SuppressWarnings("unchecked")
    private static QueryHandler<Object, Object> cast(QueryHandler<?, ?> handler) {
        return (QueryHandler<Object, Object>) handler;
    }

    private Optional<Class<?>> resolveQueryType(String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException e) {
            log.debug("unable to find the query type {}", name);
            return Optional.empty();
        }
    }

    /**
     * @param name       the name of the query
     * @param queryAsMap the query
     * @return the result of the query
     */
    @POST
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> execute(@PathParam("name") String name, Object queryAsMap) {
        val response = resolveQueryType(name)
            .map(queryType -> {
                val query = objectMapper.convertValue(queryAsMap, queryType);
                val selectedHandlers = queryHandlers.select(new HandleQualifier(queryType));

                if (selectedHandlers.isAmbiguous()) {
                    log.debug("unable to select a query handler for {}", name);
                    return CompletableFuture.completedStage(Response.status(Response.Status.NOT_FOUND).build());
                }
                val handler = cast(selectedHandlers.get());

                log.info("handle {} / {}", name, query);
                return handler.execute(query).thenApplyAsync(this::toResponse);
            })
            .orElseGet(() -> CompletableFuture.completedStage(Response.status(Response.Status.NOT_FOUND).build()));
        return Uni.createFrom().completionStage(response);
    }
}
