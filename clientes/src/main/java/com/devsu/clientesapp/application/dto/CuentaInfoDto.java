package com.devsu.clientesapp.application.dto;
import java.math.BigDecimal;
public record CuentaInfoDto(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial
) {
}
