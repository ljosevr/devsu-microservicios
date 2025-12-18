package com.devsu.cuentasapp.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Cuenta")
class CuentaTest {

    @Test
    @DisplayName("Crear cuenta con builder exitosamente")
    void testCrearCuentaConBuilder() {
        // Act
        Cuenta cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .saldoActual(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId("CLI001")
                .clienteNombre("Test Cliente")
                .build();

        // Assert
        assertNotNull(cuenta);
        assertEquals(1L, cuenta.getId());
        assertEquals("123456", cuenta.getNumeroCuenta());
        assertEquals("AHORROS", cuenta.getTipoCuenta());
        assertEquals(new BigDecimal("1000.00"), cuenta.getSaldoInicial());
        assertEquals(new BigDecimal("1000.00"), cuenta.getSaldoActual());
        assertTrue(cuenta.getEstado());
        assertEquals("CLI001", cuenta.getClienteId());
        assertEquals("Test Cliente", cuenta.getClienteNombre());
    }

    @Test
    @DisplayName("PrePersist establece estado true por defecto")
    void testPrePersistEstadoDefault() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta("789012")
                .tipoCuenta("CORRIENTE")
                .saldoInicial(new BigDecimal("500.00"))
                .build();

        // Act
        cuenta.prePersist();

        // Assert
        assertTrue(cuenta.getEstado());
    }

    @Test
    @DisplayName("PrePersist establece saldo actual igual al inicial")
    void testPrePersistSaldoActual() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta("456789")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("750.50"))
                .build();

        // Act
        cuenta.prePersist();

        // Assert
        assertEquals(cuenta.getSaldoInicial(), cuenta.getSaldoActual());
        assertEquals(new BigDecimal("750.50"), cuenta.getSaldoActual());
    }

    @Test
    @DisplayName("Modificar saldo actual de la cuenta")
    void testModificarSaldoActual() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .saldoActual(new BigDecimal("1000.00"))
                .build();

        // Act
        cuenta.setSaldoActual(new BigDecimal("1500.00"));

        // Assert
        assertEquals(new BigDecimal("1500.00"), cuenta.getSaldoActual());
    }

    @Test
    @DisplayName("Cambiar estado de la cuenta")
    void testCambiarEstadoCuenta() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .estado(true)
                .build();

        // Act
        cuenta.setEstado(false);

        // Assert
        assertFalse(cuenta.getEstado());
    }

    @Test
    @DisplayName("Cuenta puede tener saldo cero")
    void testCuentaConSaldoCero() {
        // Act
        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta("000000")
                .saldoInicial(BigDecimal.ZERO)
                .saldoActual(BigDecimal.ZERO)
                .build();

        // Assert
        assertEquals(BigDecimal.ZERO, cuenta.getSaldoInicial());
        assertEquals(BigDecimal.ZERO, cuenta.getSaldoActual());
    }

    @Test
    @DisplayName("Actualizar nombre del cliente en la cuenta")
    void testActualizarClienteNombre() {
        // Arrange
        Cuenta cuenta = Cuenta.builder()
                .clienteNombre("Nombre Original")
                .build();

        // Act
        cuenta.setClienteNombre("Nombre Actualizado");

        // Assert
        assertEquals("Nombre Actualizado", cuenta.getClienteNombre());
    }

    @Test
    @DisplayName("Tipo de cuenta puede ser AHORROS o CORRIENTE")
    void testTiposCuenta() {
        // Arrange & Act
        Cuenta cuentaAhorros = Cuenta.builder()
                .tipoCuenta("AHORROS")
                .build();

        Cuenta cuentaCorriente = Cuenta.builder()
                .tipoCuenta("CORRIENTE")
                .build();

        // Assert
        assertEquals("AHORROS", cuentaAhorros.getTipoCuenta());
        assertEquals("CORRIENTE", cuentaCorriente.getTipoCuenta());
    }
}

