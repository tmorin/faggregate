package io.morin.faggregate.api;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutputBuilderTest {

    @Mock
    Event event0;

    @Mock
    Event event1;

    @Mock
    Event event2;

    @Test
    void shouldHaveEmptyResult() {
        val output = OutputBuilder.get().add(event0).add(event1, event2).build();
        assertTrue(output.getResult().isEmpty());
        assertEquals(event0, output.getEvents().get(0));
        assertEquals(event1, output.getEvents().get(1));
        assertEquals(event2, output.getEvents().get(2));
    }

    @Test
    void shouldHaveAResult() {
        val output = OutputBuilder.get("test").build();
        assertFalse(output.getResult().isEmpty());
        assertEquals("test", output.getResult().get());
    }
}
