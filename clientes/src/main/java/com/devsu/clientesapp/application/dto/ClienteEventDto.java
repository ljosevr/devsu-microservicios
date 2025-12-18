package com.devsu.clientesapp.application.dto;

public record ClienteEventDto(
        String clienteId,
        String nombre,
        String identificacion,
        String eventType // CREATED, UPDATED, DELETED
) {
}

