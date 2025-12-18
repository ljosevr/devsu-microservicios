package com.devsu.cuentasapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCuentaDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String clienteId;
    private String clienteNombre;
    private List<CuentaEstadoDto> cuentas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CuentaEstadoDto {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoInicial;
        private BigDecimal saldoActual;
        private Boolean estado;
        private List<MovimientoEstadoDto> movimientos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MovimientoEstadoDto {
        private LocalDate fecha;
        private String tipoMovimiento;
        private BigDecimal valor;
        private BigDecimal saldo;
    }
}

