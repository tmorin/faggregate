package todo.quarkus.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class EventRecordCodec implements Codec<EventRecord> {

    ObjectMapper objectMapper;

    Codec<Document> documentCodec;

    EventRecordCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Class<EventRecord> getEncoderClass() {
        return EventRecord.class;
    }

    @SneakyThrows
    @Override
    public void encode(BsonWriter writer, EventRecord value, EncoderContext encoderContext) {
        val json = objectMapper.writeValueAsString(value);
        log.debug("encode {}", json);
        val document = Document.parse(json);
        documentCodec.encode(writer, document, encoderContext);
    }

    @SneakyThrows
    @Override
    public EventRecord decode(BsonReader reader, DecoderContext decoderContext) {
        val document = documentCodec.decode(reader, decoderContext);
        val json = document.toJson();
        log.debug("decode {}", json);
        return objectMapper.readValue(json, EventRecord.class);
    }
}
