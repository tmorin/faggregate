package todo.quarkus.repository;

import com.mongodb.client.MongoClient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class EventCounter {

    final MongoClient client;

    Long beforeCnt;
    Long afterCnt;

    @Getter
    Long diff;

    @SneakyThrows
    void count() {
        if (Objects.isNull(beforeCnt)) {
            beforeCnt =
                client
                    .getDatabase(MongodbCommandRepository.DATABASE_NAME)
                    .getCollection(MongodbCommandRepository.COLLECTION_NAME_EVENTS, EventRecord.class)
                    .countDocuments();
            log.debug("beforeCnt {}", beforeCnt);
            return;
        }
        if (Objects.isNull(afterCnt)) {
            afterCnt =
                client
                    .getDatabase(MongodbCommandRepository.DATABASE_NAME)
                    .getCollection(MongodbCommandRepository.COLLECTION_NAME_EVENTS, EventRecord.class)
                    .countDocuments();
            diff = afterCnt - beforeCnt;
            log.debug("afterCnt {}", afterCnt);
        }
    }
}
