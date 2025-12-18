package com.devsu.clientesapp.application.dto;

import jakarta.validation.constraints.*;

public record ClienteRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String nombre,

        @Size(max = 20, message = "El género no puede exceder 20 caracteres")
        String genero,

        @Min(value = 0, message = "La edad debe ser un valor positivo")
        @Max(value = 150, message = "La edad debe ser un valor válido")
        Integer edad,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(max = 20, message = "La identificación no puede exceder 20 caracteres")
        String identificacion,

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        String direccion,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @NotBlank(message = "El clienteId es obligatorio")
        String clienteId,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
        String contrasena,

        Boolean estado
) {
}

