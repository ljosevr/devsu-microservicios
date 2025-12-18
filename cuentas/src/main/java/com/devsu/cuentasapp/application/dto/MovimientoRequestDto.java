package com.devsu.cuentasapp.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoRequestDto {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento; // DEPOSITO, RETIRO

    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;
}

