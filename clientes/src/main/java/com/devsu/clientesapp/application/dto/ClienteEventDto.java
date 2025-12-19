package com.devsu.clientesapp.application.dto;

import java.util.List;

public record ClienteEventDto(
        String clienteId,
        String nombre,
        String identificacion,
        String eventType, // CREATED, UPDATED, DELETED
        List<CuentaInfoDto> cuentas // Lista de cuentas a crear (solo para CREATED)
) {
    // Constructor para eventos sin cuentas (UPDATED, DELETED)
    public ClienteEventDto(String clienteId, String nombre, String identificacion, String eventType) {
        this(clienteId, nombre, identificacion, eventType, null);
    }
}

