package com.devsu.cuentasapp.application.service;

import com.devsu.cuentasapp.application.dto.EstadoCuentaDto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ReporteService")
class ReporteServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Cuenta cuenta1;
    private Cuenta cuenta2;
    private Movimiento movimiento1;
    private Movimiento movimiento2;

    @BeforeEach
    void setUp() {
        cuenta1 = Cuenta.builder()
                .id(1L)
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .saldoActual(new BigDecimal("1200.00"))
                .estado(true)
                .clienteId("CLI001")
                .clienteNombre("Jose Lema")
                .build();

        cuenta2 = Cuenta.builder()
                .id(2L)
                .numeroCuenta("789012")
                .tipoCuenta("CORRIENTE")
                .saldoInicial(new BigDecimal("500.00"))
                .saldoActual(new BigDecimal("700.00"))
                .estado(true)
                .clienteId("CLI001")
                .clienteNombre("Jose Lema")
                .build();

        movimiento1 = Movimiento.builder()
                .id(1L)
                .fecha(LocalDateTime.now().minusDays(2))
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("200.00"))
                .saldo(new BigDecimal("1200.00"))
                .cuenta(cuenta1)
                .build();

        movimiento2 = Movimiento.builder()
                .id(2L)
                .fecha(LocalDateTime.now().minusDays(1))
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("200.00"))
                .saldo(new BigDecimal("700.00"))
                .cuenta(cuenta2)
                .build();
    }

    @Test
    @DisplayName("Generar estado de cuenta con múltiples cuentas")
    void testGenerarEstadoCuentaConMultiplesCuentas() {
        // Arrange
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        LocalDate fechaFin = LocalDate.now();

        when(cuentaRepository.findByClienteId("CLI001"))
                .thenReturn(Arrays.asList(cuenta1, cuenta2));
        when(movimientoRepository.findByCuentaIdAndFechaBetween(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(movimiento1))
                .thenReturn(Arrays.asList(movimiento2));

        // Act
        EstadoCuentaDto result = reporteService.generarEstadoCuenta("CLI001", fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertEquals("CLI001", result.getClienteId());
        assertEquals("Jose Lema", result.getClienteNombre());
        assertEquals(2, result.getCuentas().size());
        assertEquals(fechaInicio, result.getFechaInicio());
        assertEquals(fechaFin, result.getFechaFin());

        verify(cuentaRepository, times(1)).findByClienteId("CLI001");
    }

    @Test
    @DisplayName("Generar estado de cuenta sin movimientos en el rango")
    void testGenerarEstadoCuentaSinMovimientos() {
        // Arrange
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        LocalDate fechaFin = LocalDate.now();

        when(cuentaRepository.findByClienteId("CLI001"))
                .thenReturn(Arrays.asList(cuenta1));
        when(movimientoRepository.findByCuentaIdAndFechaBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        EstadoCuentaDto result = reporteService.generarEstadoCuenta("CLI001", fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCuentas().size());
        assertTrue(result.getCuentas().get(0).getMovimientos().isEmpty());
    }

    @Test
    @DisplayName("Generar estado de cuenta para cliente sin cuentas")
    void testGenerarEstadoCuentaSinCuentas() {
        // Arrange
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        LocalDate fechaFin = LocalDate.now();

        when(cuentaRepository.findByClienteId("CLI999"))
                .thenReturn(Collections.emptyList());

        // Act
        EstadoCuentaDto result = reporteService.generarEstadoCuenta("CLI999", fechaInicio, fechaFin);

        // Assert
        assertNotNull(result);
        assertEquals("CLI999", result.getClienteId());
        assertTrue(result.getCuentas().isEmpty());
    }

    @Test
    @DisplayName("Verificar que el reporte incluye información correcta de cada cuenta")
    void testReporteIncluyeInformacionCuenta() {
        // Arrange
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        LocalDate fechaFin = LocalDate.now();

        when(cuentaRepository.findByClienteId("CLI001"))
                .thenReturn(Arrays.asList(cuenta1));
        when(movimientoRepository.findByCuentaIdAndFechaBetween(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(movimiento1));

        // Act
        EstadoCuentaDto result = reporteService.generarEstadoCuenta("CLI001", fechaInicio, fechaFin);

        // Assert
        EstadoCuentaDto.CuentaEstadoDto cuentaEstado = result.getCuentas().get(0);
        assertEquals("123456", cuentaEstado.getNumeroCuenta());
        assertEquals("AHORROS", cuentaEstado.getTipoCuenta());
        assertEquals(new BigDecimal("1000.00"), cuentaEstado.getSaldoInicial());
        assertEquals(new BigDecimal("1200.00"), cuentaEstado.getSaldoActual());
        assertTrue(cuentaEstado.getEstado());
    }

    @Test
    @DisplayName("Verificar que el reporte incluye movimientos con detalles correctos")
    void testReporteIncluyeMovimientosDetallados() {
        // Arrange
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        LocalDate fechaFin = LocalDate.now();

        when(cuentaRepository.findByClienteId("CLI001"))
                .thenReturn(Arrays.asList(cuenta1));
        when(movimientoRepository.findByCuentaIdAndFechaBetween(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(movimiento1));

        // Act
        EstadoCuentaDto result = reporteService.generarEstadoCuenta("CLI001", fechaInicio, fechaFin);

        // Assert
        List<EstadoCuentaDto.MovimientoEstadoDto> movimientos =
                result.getCuentas().get(0).getMovimientos();

        assertEquals(1, movimientos.size());
        EstadoCuentaDto.MovimientoEstadoDto movDto = movimientos.get(0);
        assertEquals("DEPOSITO", movDto.getTipoMovimiento());
        assertEquals(new BigDecimal("200.00"), movDto.getValor());
        assertEquals(new BigDecimal("1200.00"), movDto.getSaldo());
        assertNotNull(movDto.getFecha());
    }
}

