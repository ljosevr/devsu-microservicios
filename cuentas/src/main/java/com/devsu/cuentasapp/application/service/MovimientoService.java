package com.devsu.cuentasapp.application.service;

import com.devsu.cuentasapp.application.dto.MovimientoRequestDto;
import com.devsu.cuentasapp.application.dto.MovimientoResponseDto;
import com.devsu.cuentasapp.application.mapper.MovimientoMapper;
import com.devsu.cuentasapp.domain.exception.CuentaNotFoundException;
import com.devsu.cuentasapp.domain.exception.SaldoInsuficienteException;
import com.devsu.cuentasapp.domain.model.Cuenta;
import com.devsu.cuentasapp.domain.model.Movimiento;
import com.devsu.cuentasapp.domain.repository.CuentaRepository;
import com.devsu.cuentasapp.domain.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    @Transactional
    public MovimientoResponseDto registrarMovimiento(MovimientoRequestDto requestDto) {
        log.info("Registrando movimiento para cuenta: {}", requestDto.getNumeroCuenta());

        // Obtener la cuenta
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(requestDto.getNumeroCuenta())
                .orElseThrow(() -> new CuentaNotFoundException("numeroCuenta", requestDto.getNumeroCuenta()));

        // Validar que la cuenta esté activa
        if (!cuenta.getEstado()) {
            throw new IllegalStateException("La cuenta está inactiva");
        }

        // Determinar el valor del movimiento
        BigDecimal valorMovimiento = requestDto.getValor();
        String tipoMovimiento = requestDto.getTipoMovimiento().toUpperCase();

        // Calcular nuevo saldo
        BigDecimal nuevoSaldo;
        if ("RETIRO".equals(tipoMovimiento)) {
            // Para retiros, el valor debe ser negativo
            if (valorMovimiento.compareTo(BigDecimal.ZERO) > 0) {
                valorMovimiento = valorMovimiento.negate();
            }
            nuevoSaldo = cuenta.getSaldoActual().add(valorMovimiento);

            // Validar saldo suficiente (F3)
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                log.warn("Saldo insuficiente para retiro. Saldo actual: {}, Valor retiro: {}",
                        cuenta.getSaldoActual(), valorMovimiento.abs());
                throw new SaldoInsuficienteException();
            }
        } else if ("DEPOSITO".equals(tipoMovimiento)) {
            // Para depósitos, el valor debe ser positivo
            if (valorMovimiento.compareTo(BigDecimal.ZERO) < 0) {
                valorMovimiento = valorMovimiento.abs();
            }
            nuevoSaldo = cuenta.getSaldoActual().add(valorMovimiento);
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido: " + tipoMovimiento);
        }

        // Crear el movimiento
        Movimiento movimiento = Movimiento.builder()
                .fecha(LocalDateTime.now())
                .tipoMovimiento(tipoMovimiento)
                .valor(valorMovimiento)
                .saldo(nuevoSaldo)
                .cuenta(cuenta)
                .build();

        // Actualizar saldo de la cuenta
        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // Guardar el movimiento
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);

        log.info("Movimiento registrado exitosamente. Nuevo saldo: {}", nuevoSaldo);
        return movimientoMapper.toDto(savedMovimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDto> obtenerTodosLosMovimientos() {
        log.info("Obteniendo todos los movimientos");
        return movimientoRepository.findAll().stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDto obtenerMovimientoPorId(Long id) {
        log.info("Obteniendo movimiento por id: {}", id);
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));
        return movimientoMapper.toDto(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDto> obtenerMovimientosPorCuenta(String numeroCuenta) {
        log.info("Obteniendo movimientos por cuenta: {}", numeroCuenta);

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("numeroCuenta", numeroCuenta));

        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuenta.getId()).stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }
}

