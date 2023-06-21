package todo.quarkus.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Providers of codecs to help the MongoDB client to process {@link EventRecord} and {@link EventRecord}.
 */
@ApplicationScoped
class TodoCodecProvider implements CodecProvider {

    @Inject
    ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(StateRecord.class)) {
            return (Codec<T>) new StateRecordCodec(objectMapper);
        }
        if (clazz.equals(EventRecord.class)) {
            return (Codec<T>) new EventRecordCodec(objectMapper);
        }
        return null;
    }
}
