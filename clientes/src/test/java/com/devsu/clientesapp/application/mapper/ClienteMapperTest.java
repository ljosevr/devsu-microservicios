package com.devsu.clientesapp.application.mapper;

import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;
import com.devsu.clientesapp.domain.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ClienteMapper")
class ClienteMapperTest {

    private ClienteMapper clienteMapper;

    @BeforeEach
    void setUp() {
        clienteMapper = new ClienteMapper();
    }

    @Test
    @DisplayName("Convertir RequestDto a Entity exitosamente")
    void testToEntity() {
        // Arrange
        ClienteRequestDto requestDto = new ClienteRequestDto(
                "Juan Perez",
                "Masculino",
                25,
                "9876543210",
                "Calle Principal 123",
                "099887766",
                "CLI999",
                "pass123",
                true
        );

        // Act
        Cliente cliente = clienteMapper.toEntity(requestDto);

        // Assert
        assertNotNull(cliente);
        assertEquals("Juan Perez", cliente.getNombre());
        assertEquals("Masculino", cliente.getGenero());
        assertEquals(25, cliente.getEdad());
        assertEquals("9876543210", cliente.getIdentificacion());
        assertEquals("Calle Principal 123", cliente.getDireccion());
        assertEquals("099887766", cliente.getTelefono());
        assertEquals("CLI999", cliente.getClienteId());
        assertEquals("pass123", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }

    @Test
    @DisplayName("Convertir RequestDto con estado null a Entity con estado true por defecto")
    void testToEntityConEstadoNull() {
        // Arrange
        ClienteRequestDto requestDto = new ClienteRequestDto(
                "Maria Lopez",
                "Femenino",
                30,
                "1122334455",
                "Av. Siempre Viva",
                "098765432",
                "CLI888",
                "secret",
                null // Estado null
        );

        // Act
        Cliente cliente = clienteMapper.toEntity(requestDto);

        // Assert
        assertNotNull(cliente);
        assertTrue(cliente.getEstado(), "Estado debe ser true por defecto");
    }

    @Test
    @DisplayName("Convertir Entity a ResponseDto exitosamente")
    void testToDto() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .id(10L)
                .nombre("Carlos Ruiz")
                .genero("Masculino")
                .edad(40)
                .identificacion("5544332211")
                .direccion("Norte 456")
                .telefono("097654321")
                .clienteId("CLI777")
                .contrasena("encrypted")
                .estado(false)
                .build();

        // Act
        ClienteResponseDto responseDto = clienteMapper.toDto(cliente);

        // Assert
        assertNotNull(responseDto);
        assertEquals(10L, responseDto.id());
        assertEquals("Carlos Ruiz", responseDto.nombre());
        assertEquals("Masculino", responseDto.genero());
        assertEquals(40, responseDto.edad());
        assertEquals("5544332211", responseDto.identificacion());
        assertEquals("Norte 456", responseDto.direccion());
        assertEquals("097654321", responseDto.telefono());
        assertEquals("CLI777", responseDto.clienteId());
        assertFalse(responseDto.estado());
    }

    @Test
    @DisplayName("Actualizar Entity desde RequestDto con todos los campos")
    void testUpdateEntityFromDto() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .id(5L)
                .nombre("Nombre Original")
                .genero("Masculino")
                .edad(20)
                .identificacion("1111111111")
                .direccion("Dir Original")
                .telefono("011111111")
                .clienteId("CLIOLD")
                .contrasena("oldpass")
                .estado(true)
                .build();

        ClienteRequestDto requestDto = new ClienteRequestDto(
                "Nombre Actualizado",
                "Femenino",
                35,
                "2222222222",
                "Dir Actualizada",
                "099999999",
                "CLINEW",
                "newpass",
                false
        );

        // Act
        clienteMapper.updateEntityFromDto(requestDto, cliente);

        // Assert
        assertEquals("Nombre Actualizado", cliente.getNombre());
        assertEquals("Femenino", cliente.getGenero());
        assertEquals(35, cliente.getEdad());
        assertEquals("2222222222", cliente.getIdentificacion());
        assertEquals("Dir Actualizada", cliente.getDireccion());
        assertEquals("099999999", cliente.getTelefono());
        assertEquals("newpass", cliente.getContrasena());
        assertFalse(cliente.getEstado());
    }

    @Test
    @DisplayName("Actualizar Entity desde RequestDto con campos null no modifica el Entity")
    void testUpdateEntityFromDtoConCamposNull() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .id(3L)
                .nombre("Nombre Original")
                .genero("Masculino")
                .edad(28)
                .identificacion("9999999999")
                .direccion("Dir Original")
                .telefono("088888888")
                .clienteId("CLIKEEP")
                .contrasena("keeppass")
                .estado(true)
                .build();

        ClienteRequestDto requestDto = new ClienteRequestDto(
                null, // nombre null
                null, // genero null
                null, // edad null
                null, // identificacion null
                null, // direccion null
                null, // telefono null
                "CLIKEEP",
                null, // contrasena null
                null  // estado null
        );

        // Act
        clienteMapper.updateEntityFromDto(requestDto, cliente);

        // Assert - Los valores originales deben mantenerse
        assertEquals("Nombre Original", cliente.getNombre());
        assertEquals("Masculino", cliente.getGenero());
        assertEquals(28, cliente.getEdad());
        assertEquals("9999999999", cliente.getIdentificacion());
        assertEquals("Dir Original", cliente.getDireccion());
        assertEquals("088888888", cliente.getTelefono());
        assertEquals("keeppass", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }
}

