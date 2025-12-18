package com.devsu.cuentasapp.infrastructure.messaging;

import com.devsu.cuentasapp.application.dto.ClienteEventDto;
import com.devsu.cuentasapp.domain.model.Cuenta;
import com.devsu.cuentasapp.domain.repository.CuentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class ClienteEventListener {

    private final CuentaRepository cuentaRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.queue.name:cliente-queue}")
    public void handleClienteEvent(String message) {
        try {
            ClienteEventDto event = objectMapper.readValue(message, ClienteEventDto.class);
            log.info("Evento de cliente recibido: {} - {}", event.getEventType(), event.getClienteId());

            switch (event.getEventType()) {
                case "CREATED":
                    handleClienteCreated(event);
                    break;
                case "UPDATED":
                    handleClienteUpdated(event);
                    break;
                case "DELETED":
                    handleClienteDeleted(event);
                    break;
                default:
                    log.warn("Tipo de evento no manejado: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error al procesar evento de cliente: {}", e.getMessage(), e);
        }
    }

    private void handleClienteCreated(ClienteEventDto event) {
        log.info("Cliente creado: {} - {}", event.getClienteId(), event.getNombre());
        // Se podría actualizar información adicional si es necesario
    }

    private void handleClienteUpdated(ClienteEventDto event) {
        log.info("Cliente actualizado: {} - {}", event.getClienteId(), event.getNombre());

        // Actualizar el nombre del cliente en todas sus cuentas
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(event.getClienteId());
        for (Cuenta cuenta : cuentas) {
            cuenta.setClienteNombre(event.getNombre());
            cuentaRepository.save(cuenta);
        }
    }

    private void handleClienteDeleted(ClienteEventDto event) {
        log.info("Cliente eliminado: {}", event.getClienteId());
        // Se podría marcar las cuentas como inactivas o eliminarlas
    }
}

