package todo.quarkus.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.query.QueryHandler;

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

    @POST
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> execute(@PathParam("name") String name, Object queryAsMap) {
        val response = resolveQueryType(name)
            .map(type -> {
                val query = objectMapper.convertValue(queryAsMap, type);
                val selectedHandlers = queryHandlers.select(new HandleQualifier(type));

                if (selectedHandlers.isAmbiguous()) {
                    log.debug("unable to select a query handler for {}", name);
                    return CompletableFuture.completedStage(Response.status(Response.Status.NOT_FOUND).build());
                }
                val handler = cast(selectedHandlers.get());

                log.info("handle {} / {}", name, query);
                return handler.execute(query).thenApply(this::toResponse);
            })
            .orElseGet(() -> CompletableFuture.completedStage(Response.status(Response.Status.NOT_FOUND).build()));
        return Uni.createFrom().completionStage(response);
    }
}
