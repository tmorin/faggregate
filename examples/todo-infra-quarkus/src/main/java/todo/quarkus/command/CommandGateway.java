package todo.quarkus.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.morin.faggregate.api.AggregateManager;
import io.morin.faggregate.api.Output;
import io.morin.faggregate.simple.core.AggregateNotFound;
import io.smallrye.mutiny.Uni;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import todo.model.TodoListId;
import todo.model.command.TodoListCommand;

/**
 * The gateway process the commands.
 */
@Slf4j
@Path("/command")
public class CommandGateway {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AggregateManager<TodoListId> aggregateManager;

    private Optional<Class<?>> resolveCommandType(String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException e) {
            log.debug("unable to find the command type {}", name);
            return Optional.empty();
        }
    }

    private <R> CompletionStage<Response> toResponse(Output<R> output) {
        return CompletableFuture.completedStage(
            output.getResult().map(Response::ok).orElseGet(Response::noContent).build()
        );
    }

    @ServerExceptionMapper
    public RestResponse<String> mapAggregateNotFound(AggregateNotFound exception) {
        return RestResponse.status(Response.Status.NOT_FOUND, exception.getMessage());
    }

    /**
     * @param name         the name of the command
     * @param commandAsMap the command
     * @return the result of the command
     */
    @POST
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> executeAlt(@PathParam("name") String name, Object commandAsMap) {
        val response = resolveCommandType(name)
            .map(commandType -> {
                val command = objectMapper.convertValue(commandAsMap, commandType);
                if (command instanceof TodoListCommand) {
                    log.info("handle {} / {}", name, command);
                    return aggregateManager
                        .execute(((TodoListCommand) command).getTodoListId(), command)
                        .thenComposeAsync(this::toResponse);
                }
                log.debug("the command body of {} is not an instance of {}", name, TodoListCommand.class.getName());
                return CompletableFuture.completedStage(Response.status(Response.Status.BAD_REQUEST).build());
            })
            .orElseGet(() -> CompletableFuture.completedStage(Response.status(Response.Status.NOT_FOUND).build()));
        return Uni.createFrom().completionStage(response);
    }
}
