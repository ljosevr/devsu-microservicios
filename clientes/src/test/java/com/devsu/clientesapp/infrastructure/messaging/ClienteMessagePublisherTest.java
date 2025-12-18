package com.devsu.clientesapp.infrastructure.messaging;

import com.devsu.clientesapp.application.dto.ClienteEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ClienteMessagePublisher")
class ClienteMessagePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ClienteMessagePublisher messagePublisher;

    @BeforeEach
    void setUp() {
        // Configurar valores de las propiedades usando ReflectionTestUtils
        ReflectionTestUtils.setField(messagePublisher, "exchange", "test-exchange");
        ReflectionTestUtils.setField(messagePublisher, "routingKey", "test.routing.key");
    }

    @Test
    @DisplayName("Publicar evento de cliente creado exitosamente")
    void testPublishClienteEventCreado() throws Exception {
        // Arrange
        ClienteEventDto event = new ClienteEventDto(
                "CLI001",
                "Jose Lema",
                "1234567890",
                "CREATED"
        );
        String jsonMessage = "{\"clienteId\":\"CLI001\",\"nombre\":\"Jose Lema\"}";

        when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        // Act
        messagePublisher.publishClienteEvent(event);

        // Assert
        verify(objectMapper, times(1)).writeValueAsString(event);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("test-exchange"),
                eq("test.routing.key"),
                eq(jsonMessage)
        );
    }

    @Test
    @DisplayName("Publicar evento de cliente actualizado")
    void testPublishClienteEventActualizado() throws Exception {
        // Arrange
        ClienteEventDto event = new ClienteEventDto(
                "CLI002",
                "Maria Lopez",
                "9876543210",
                "UPDATED"
        );
        String jsonMessage = "{\"clienteId\":\"CLI002\"}";

        when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

        // Act
        messagePublisher.publishClienteEvent(event);

        // Assert
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(rabbitTemplate).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals("test-exchange", exchangeCaptor.getValue());
        assertEquals("test.routing.key", routingKeyCaptor.getValue());
        assertEquals(jsonMessage, messageCaptor.getValue());
    }

    @Test
    @DisplayName("Publicar evento de cliente eliminado")
    void testPublishClienteEventEliminado() throws Exception {
        // Arrange
        ClienteEventDto event = new ClienteEventDto(
                "CLI003",
                null,
                null,
                "DELETED"
        );
        String jsonMessage = "{\"clienteId\":\"CLI003\",\"eventType\":\"DELETED\"}";

        when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

        // Act
        messagePublisher.publishClienteEvent(event);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(
                anyString(),
                anyString(),
                eq(jsonMessage)
        );
    }

    @Test
    @DisplayName("Manejar excepción al serializar evento")
    void testPublishClienteEventConErrorSerializacion() throws Exception {
        // Arrange
        ClienteEventDto event = new ClienteEventDto(
                "CLI004",
                "Cliente Error",
                "1111111111",
                "CREATED"
        );

        when(objectMapper.writeValueAsString(event))
                .thenThrow(new RuntimeException("Error de serialización"));

        // Act - No debe lanzar excepción, debe loguear el error
        assertDoesNotThrow(() -> messagePublisher.publishClienteEvent(event));

        // Assert
        verify(objectMapper, times(1)).writeValueAsString(event);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Verificar que se usa el exchange y routing key configurados")
    void testUsaConfiguracionCorrecta() throws Exception {
        // Arrange
        ClienteEventDto event = new ClienteEventDto(
                "CLI005",
                "Test Config",
                "5555555555",
                "CREATED"
        );
        when(objectMapper.writeValueAsString(event)).thenReturn("{}");

        // Act
        messagePublisher.publishClienteEvent(event);

        // Assert - Verificar que se llamó con los valores configurados
        verify(rabbitTemplate).convertAndSend(
                eq("test-exchange"),
                eq("test.routing.key"),
                anyString()
        );
    }
}

