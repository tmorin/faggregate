package todo.quarkus.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

@ApplicationScoped
public class TodoCodecProvider implements CodecProvider {

    @Inject
    ObjectMapper objectMapper;

    @Override
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
