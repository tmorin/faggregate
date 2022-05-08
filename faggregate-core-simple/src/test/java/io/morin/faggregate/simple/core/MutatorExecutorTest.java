package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MutatorExecutorTest {

    @Mock
    Serializable event;

    @Test
    void shouldExecute() {
        val mutatorExecutor = new MutatorExecutor<String, Serializable>(
            event,
            (state, event) -> String.format("%s - %s", state, event)
        );
        val newState = mutatorExecutor.execute("initial");
        assertEquals("initial - event", newState);
    }
}
