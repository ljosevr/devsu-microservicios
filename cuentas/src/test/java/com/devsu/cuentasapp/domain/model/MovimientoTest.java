package com.devsu.cuentasapp.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Movimiento")
class MovimientoTest {

    @Test
    @DisplayName("Crear movimiento de depósito exitosamente")
    void testCrearMovimientoDeposito() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("123456")
                .saldoActual(new BigDecimal("1000.00"))
                .build();

        // Act
        Movimiento movimiento = Movimiento.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("500.00"))
                .saldo(new BigDecimal("1500.00"))
                .cuenta(cuenta)
                .build();

        // Assert
        assertNotNull(movimiento);
        assertEquals("DEPOSITO", movimiento.getTipoMovimiento());
        assertEquals(new BigDecimal("500.00"), movimiento.getValor());
        assertEquals(new BigDecimal("1500.00"), movimiento.getSaldo());
        assertTrue(movimiento.getValor().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Crear movimiento de retiro con valor negativo")
    void testCrearMovimientoRetiro() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("123456")
                .saldoActual(new BigDecimal("1000.00"))
                .build();

        // Act
        Movimiento movimiento = Movimiento.builder()
                .id(2L)
                .fecha(LocalDateTime.now())
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("-300.00"))
                .saldo(new BigDecimal("700.00"))
                .cuenta(cuenta)
                .build();

        // Assert
        assertNotNull(movimiento);
        assertEquals("RETIRO", movimiento.getTipoMovimiento());
        assertEquals(new BigDecimal("-300.00"), movimiento.getValor());
        assertEquals(new BigDecimal("700.00"), movimiento.getSaldo());
        assertTrue(movimiento.getValor().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Movimiento tiene fecha de creación")
    void testMovimientoTieneFecha() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        Movimiento movimiento = Movimiento.builder()
                .fecha(ahora)
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("100.00"))
                .build();

        // Assert
        assertNotNull(movimiento.getFecha());
        assertEquals(ahora, movimiento.getFecha());
    }

    @Test
    @DisplayName("Movimiento está asociado a una cuenta")
    void testMovimientoAsociadoACuenta() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .id(5L)
                .numeroCuenta("999999")
                .build();

        // Act
        Movimiento movimiento = Movimiento.builder()
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("250.00"))
                .cuenta(cuenta)
                .build();

        // Assert
        assertNotNull(movimiento.getCuenta());
        assertEquals(5L, movimiento.getCuenta().getId());
        assertEquals("999999", movimiento.getCuenta().getNumeroCuenta());
    }

    @Test
    @DisplayName("Saldo después del movimiento es correcto")
    void testSaldoDespuesDelMovimiento() {
        // Arrange
        BigDecimal saldoAnterior = new BigDecimal("800.00");
        BigDecimal valorDeposito = new BigDecimal("200.00");
        BigDecimal saldoNuevo = saldoAnterior.add(valorDeposito);

        // Act
        Movimiento movimiento = Movimiento.builder()
                .tipoMovimiento("DEPOSITO")
                .valor(valorDeposito)
                .saldo(saldoNuevo)
                .build();

        // Assert
        assertEquals(new BigDecimal("1000.00"), movimiento.getSaldo());
    }

    @Test
    @DisplayName("Comparar dos movimientos diferentes")
    void testCompararDosMovimientos() {
        // Arrange & Act
        Movimiento mov1 = Movimiento.builder()
                .id(1L)
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("100.00"))
                .build();

        Movimiento mov2 = Movimiento.builder()
                .id(2L)
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("-50.00"))
                .build();

        // Assert
        assertNotEquals(mov1.getId(), mov2.getId());
        assertNotEquals(mov1.getTipoMovimiento(), mov2.getTipoMovimiento());
        assertNotEquals(mov1.getValor(), mov2.getValor());
    }

    @Test
    @DisplayName("Movimiento puede tener valor decimal preciso")
    void testMovimientoConValorDecimal() {
        // Act
        Movimiento movimiento = Movimiento.builder()
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("123.45"))
                .saldo(new BigDecimal("1123.45"))
                .build();

        // Assert
        assertEquals(new BigDecimal("123.45"), movimiento.getValor());
        assertEquals(2, movimiento.getValor().scale());
    }
}

