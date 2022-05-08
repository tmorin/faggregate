package todo.quarkus.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.Output;
import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;

@Slf4j
@Path("/command")
public class CommandGateway {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AggregateManager<TodoListId> aggregateManager;

    private <R> CompletionStage<Response> toResponse(Output<R> output) {
        return CompletableFuture.completedStage(
            output.getResult().map(Response::ok).orElseGet(Response::noContent).build()
        );
    }

    private CompletionStage<Response> handle(String name, Object queryAsMap) {
        val descriptor = CommandDescriptor.valueOf(name);
        val command = objectMapper.convertValue(queryAsMap, descriptor.type);
        return aggregateManager.execute(command.getTodoListId(), command).thenComposeAsync(this::toResponse);
    }

    @POST
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> execute(@PathParam("name") String name, Object queryAsMap) {
        log.debug("handle the command {} / {}", name, queryAsMap);
        val response = handle(name, queryAsMap);
        return Uni.createFrom().completionStage(response);
    }
}
