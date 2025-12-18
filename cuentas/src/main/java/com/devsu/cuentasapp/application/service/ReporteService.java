package com.devsu.cuentasapp.application.service;

import com.devsu.cuentasapp.application.dto.EstadoCuentaDto;
import com.devsu.cuentasapp.domain.model.Cuenta;
import com.devsu.cuentasapp.domain.model.Movimiento;
import com.devsu.cuentasapp.domain.repository.CuentaRepository;
import com.devsu.cuentasapp.domain.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Transactional(readOnly = true)
    public EstadoCuentaDto generarEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Generando estado de cuenta para cliente: {} desde {} hasta {}",
                clienteId, fechaInicio, fechaFin);

        // Obtener todas las cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        if (cuentas.isEmpty()) {
            log.warn("No se encontraron cuentas para el cliente: {}", clienteId);
        }

        // Convertir fechas a LocalDateTime
        LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime fechaFinDateTime = fechaFin.atTime(LocalTime.MAX);

        // Construir el reporte
        String clienteNombre = cuentas.isEmpty() ? "" : cuentas.get(0).getClienteNombre();

        List<EstadoCuentaDto.CuentaEstadoDto> cuentasEstado = cuentas.stream()
                .map(cuenta -> {
                    // Obtener movimientos de la cuenta en el rango de fechas
                    List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                            cuenta.getId(), fechaInicioDateTime, fechaFinDateTime);

                    List<EstadoCuentaDto.MovimientoEstadoDto> movimientosEstado = movimientos.stream()
                            .map(mov -> EstadoCuentaDto.MovimientoEstadoDto.builder()
                                    .fecha(mov.getFecha().toLocalDate())
                                    .tipoMovimiento(mov.getTipoMovimiento())
                                    .valor(mov.getValor())
                                    .saldo(mov.getSaldo())
                                    .build())
                            .collect(Collectors.toList());

                    return EstadoCuentaDto.CuentaEstadoDto.builder()
                            .numeroCuenta(cuenta.getNumeroCuenta())
                            .tipoCuenta(cuenta.getTipoCuenta())
                            .saldoInicial(cuenta.getSaldoInicial())
                            .saldoActual(cuenta.getSaldoActual())
                            .estado(cuenta.getEstado())
                            .movimientos(movimientosEstado)
                            .build();
                })
                .collect(Collectors.toList());

        return EstadoCuentaDto.builder()
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .clienteId(clienteId)
                .clienteNombre(clienteNombre)
                .cuentas(cuentasEstado)
                .build();
    }
}

