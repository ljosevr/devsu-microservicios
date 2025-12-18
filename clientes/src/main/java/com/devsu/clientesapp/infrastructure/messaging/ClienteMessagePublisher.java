package com.devsu.clientesapp.infrastructure.messaging;

import com.devsu.clientesapp.application.dto.ClienteEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class ClienteMessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange.name:cliente-exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key:cliente.event}")
    private String routingKey;

    public void publishClienteEvent(ClienteEventDto event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("Evento de cliente publicado: {} - {}", event.eventType(), event.clienteId());
        } catch (Exception e) {
            log.error("Error al publicar evento de cliente: {}", e.getMessage(), e);
        }
    }
}

