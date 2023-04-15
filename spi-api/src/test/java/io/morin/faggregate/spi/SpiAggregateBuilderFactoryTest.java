package io.morin.faggregate.spi;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpiAggregateBuilderFactoryTest {

    @Mock
    ServiceLoader<Object> mockedServiceLoader;

    @Mock
    Iterator<Object> mockedIterator;

    @Mock
    SpiAggregateBuilderProvider<Object, Object> mockedProvider;

    @Mock
    Logger mockedLogger;

    @Test
    void shouldCreateAndLogWarning() {
        when(mockedServiceLoader.iterator()).thenReturn(mockedIterator);
        when(mockedIterator.hasNext()).thenReturn(true, true, true, false);
        when(mockedIterator.next()).thenReturn(mockedProvider);
        try (val mockedServiceLoaderStatic = mockStatic(ServiceLoader.class)) {
            try (val mockedLoggerStatic = mockStatic(Logger.class)) {
                mockedServiceLoaderStatic
                    .when(() -> ServiceLoader.load(ArgumentMatchers.any()))
                    .thenReturn(mockedServiceLoader);
                mockedLoggerStatic.when(() -> Logger.getLogger(ArgumentMatchers.any())).thenReturn(mockedLogger);

                val builder = SpiAggregateBuilderFactory.get();

                builder.create();
                verify(mockedLogger, only()).warning("More than one ContainerProvider are defined!");

                builder.create();
                verify(mockedLogger, only()).warning("More than one ContainerProvider are defined!");
            }
        }
    }

    @Test
    void shouldThrowException() {
        val builder = SpiAggregateBuilderFactory.get();
        assertThrows(IllegalStateException.class, builder::create);
    }
}
