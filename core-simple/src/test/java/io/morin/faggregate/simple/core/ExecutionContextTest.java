package io.morin.faggregate.simple.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;

class ExecutionContextTest {

    @Test
    void shouldSetAndGetProperties() {
        val context = ExecutionContext.create("test", "test");
        context.set("foo", "bar");
        assertEquals("bar", context.get("foo").orElseThrow());
    }
}
