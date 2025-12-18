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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MovimientoService")
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoMapper movimientoMapper;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;
    private MovimientoRequestDto depositoRequest;
    private MovimientoRequestDto retiroRequest;

    @BeforeEach
    void setUp() {
        cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .saldoActual(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId("CLI001")
                .build();

        depositoRequest = MovimientoRequestDto.builder()
                .numeroCuenta("123456")
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("500.00"))
                .build();

        retiroRequest = MovimientoRequestDto.builder()
                .numeroCuenta("123456")
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("300.00"))
                .build();
    }

    @Test
    @DisplayName("Registrar depósito exitosamente")
    void testRegistrarDeposito() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Movimiento movimiento = Movimiento.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("500.00"))
                .saldo(new BigDecimal("1500.00"))
                .cuenta(cuenta)
                .build();

        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        MovimientoResponseDto responseDto = MovimientoResponseDto.builder()
                .id(1L)
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("500.00"))
                .saldo(new BigDecimal("1500.00"))
                .numeroCuenta("123456")
                .build();

        when(movimientoMapper.toDto(any(Movimiento.class))).thenReturn(responseDto);

        // Act
        MovimientoResponseDto result = movimientoService.registrarMovimiento(depositoRequest);

        // Assert
        assertNotNull(result);
        assertEquals("DEPOSITO", result.getTipoMovimiento());
        assertEquals(new BigDecimal("500.00"), result.getValor());
        assertEquals(new BigDecimal("1500.00"), result.getSaldo());

        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Registrar retiro exitosamente")
    void testRegistrarRetiro() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Movimiento movimiento = Movimiento.builder()
                .id(2L)
                .fecha(LocalDateTime.now())
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("-300.00"))
                .saldo(new BigDecimal("700.00"))
                .cuenta(cuenta)
                .build();

        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        MovimientoResponseDto responseDto = MovimientoResponseDto.builder()
                .id(2L)
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("-300.00"))
                .saldo(new BigDecimal("700.00"))
                .numeroCuenta("123456")
                .build();

        when(movimientoMapper.toDto(any(Movimiento.class))).thenReturn(responseDto);

        // Act
        MovimientoResponseDto result = movimientoService.registrarMovimiento(retiroRequest);

        // Assert
        assertNotNull(result);
        assertEquals("RETIRO", result.getTipoMovimiento());
        assertTrue(result.getValor().compareTo(BigDecimal.ZERO) < 0);

        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando saldo es insuficiente")
    void testRetiroConSaldoInsuficiente() {
        // Arrange
        MovimientoRequestDto retiroGrande = MovimientoRequestDto.builder()
                .numeroCuenta("123456")
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("1500.00"))
                .build();

        when(cuentaRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class, () -> {
            movimientoService.registrarMovimiento(retiroGrande);
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando cuenta no existe")
    void testRegistrarMovimientoConCuentaInexistente() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("999999")).thenReturn(Optional.empty());

        MovimientoRequestDto request = MovimientoRequestDto.builder()
                .numeroCuenta("999999")
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("100.00"))
                .build();

        // Act & Assert
        assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.registrarMovimiento(request);
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando cuenta está inactiva")
    void testRegistrarMovimientoEnCuentaInactiva() {
        // Arrange
        cuenta.setEstado(false);
        when(cuentaRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            movimientoService.registrarMovimiento(depositoRequest);
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }
}

