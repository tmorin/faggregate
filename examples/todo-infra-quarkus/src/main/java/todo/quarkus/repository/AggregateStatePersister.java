package todo.quarkus.repository;

import static todo.quarkus.repository.MongodbCommandRepository.CONTEXT_KEY_VERSION;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.morin.faggregate.api.Context;
import io.morin.faggregate.simple.core.AggregateNotFound;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import todo.model.TodoListId;
import todo.model.command.TodoList;

/**
 * The persister persists the state of the aggregate.
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class AggregateStatePersister {

    ClientSession session;

    MongoCollection<StateRecord> collection;

    Context<TodoListId, ?> context;

    TodoList state;

    boolean deletion;

    CompletableFuture<Void> persist() {
        val currentSnapshotVersion = context.<String, Integer>get(CONTEXT_KEY_VERSION).orElse(0);
        val newSnapshotVersion = currentSnapshotVersion + 1;
        log.debug("SnapshotVersion {} vs {}", currentSnapshotVersion, newSnapshotVersion);

        val options = new ReplaceOptions();
        options.upsert(currentSnapshotVersion < 1);

        val idFilter = Filters.eq("body.todoListId.uuid", state.todoListId().getUuid().toString());
        val versionFilter = Filters.eq("version", currentSnapshotVersion);
        val deletionFilter = Filters.eq("deleted", false);
        val filter = Filters.and(idFilter, versionFilter, deletionFilter);

        val stateRecord = new StateRecord(newSnapshotVersion, deletion, state);
        log.debug("persist {}", stateRecord);

        val updateResult = collection.replaceOne(session, filter, stateRecord, options);

        if (!options.isUpsert() && (updateResult.getMatchedCount() < 1)) {
            return CompletableFuture.failedFuture(new AggregateNotFound(state.todoListId().toString()));
        }

        return CompletableFuture.completedFuture(null);
    }
}
