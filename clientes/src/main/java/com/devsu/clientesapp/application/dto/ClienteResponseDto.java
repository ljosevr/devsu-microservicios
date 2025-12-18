package com.devsu.clientesapp.application.dto;

public record ClienteResponseDto(
        Long id,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        String clienteId,
        Boolean estado
) {
}

