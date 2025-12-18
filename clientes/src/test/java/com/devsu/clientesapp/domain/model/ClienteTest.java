package com.devsu.clientesapp.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Cliente")
class ClienteTest {

    @Test
    @DisplayName("Crear cliente con builder exitosamente")
    void testCrearClienteConBuilder() {
        // Act
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Test Cliente")
                .genero("Masculino")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Test Address")
                .telefono("0987654321")
                .clienteId("CLI001")
                .contrasena("password123")
                .estado(true)
                .build();

        // Assert
        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("Test Cliente", cliente.getNombre());
        assertEquals("Masculino", cliente.getGenero());
        assertEquals(30, cliente.getEdad());
        assertEquals("1234567890", cliente.getIdentificacion());
        assertEquals("Test Address", cliente.getDireccion());
        assertEquals("0987654321", cliente.getTelefono());
        assertEquals("CLI001", cliente.getClienteId());
        assertEquals("password123", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }

    @Test
    @DisplayName("Cliente hereda correctamente de Persona")
    void testClienteHeredaDePersona() {
        // Arrange & Act
        Cliente cliente = Cliente.builder()
                .nombre("Herencia Test")
                .identificacion("9876543210")
                .clienteId("CLIHRD")
                .build();

        // Assert - Verificar que tiene acceso a campos de Persona
        assertNotNull(cliente.getNombre());
        assertNotNull(cliente.getIdentificacion());
        assertTrue(cliente instanceof Persona, "Cliente debe heredar de Persona");
    }

    @Test
    @DisplayName("Modificar estado del cliente")
    void testModificarEstadoCliente() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .clienteId("CLI002")
                .estado(true)
                .build();

        // Act
        cliente.setEstado(false);

        // Assert
        assertFalse(cliente.getEstado());
    }

    @Test
    @DisplayName("Modificar contraseña del cliente")
    void testModificarContrasenaCliente() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .clienteId("CLI003")
                .contrasena("oldPassword")
                .build();

        // Act
        cliente.setContrasena("newSecurePassword");

        // Assert
        assertEquals("newSecurePassword", cliente.getContrasena());
        assertNotEquals("oldPassword", cliente.getContrasena());
    }

    @Test
    @DisplayName("Cliente puede tener clienteId único")
    void testClienteIdUnico() {
        // Arrange
        Cliente cliente1 = Cliente.builder()
                .clienteId("CLI100")
                .build();

        Cliente cliente2 = Cliente.builder()
                .clienteId("CLI200")
                .build();

        // Assert
        assertNotEquals(cliente1.getClienteId(), cliente2.getClienteId());
    }
}

