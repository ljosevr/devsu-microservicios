package com.devsu.clientesapp.application.service;

import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;
import com.devsu.clientesapp.application.mapper.ClienteMapper;
import com.devsu.clientesapp.domain.exception.ClienteAlreadyExistsException;
import com.devsu.clientesapp.domain.exception.ClienteNotFoundException;
import com.devsu.clientesapp.domain.model.Cliente;
import com.devsu.clientesapp.domain.repository.ClienteRepository;
import com.devsu.clientesapp.infrastructure.messaging.ClienteMessagePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteMessagePublisher messagePublisher;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteRequestDto requestDto;
    private Cliente cliente;
    private ClienteResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Usar constructor de Record en lugar de Builder
        requestDto = new ClienteRequestDto(
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "098254785",
                "CLI001",
                "1234",
                true
        );

        cliente = Cliente.builder()
                .id(1L)
                .nombre("Jose Lema")
                .genero("Masculino")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .clienteId("CLI001")
                .contrasena("1234")
                .estado(true)
                .build();

        // Usar constructor de Record en lugar de Builder
        responseDto = new ClienteResponseDto(
                1L,
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "098254785",
                "CLI001",
                true
        );
    }

    @Test
    @DisplayName("Crear cliente exitosamente")
    void testCrearClienteExitosamente() {
        // Arrange
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(false);
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(false);
        when(clienteMapper.toEntity(any(ClienteRequestDto.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(responseDto);

        // Act - usar método crear() en lugar de crearCliente()
        ClienteResponseDto result = clienteService.crear(requestDto);

        // Assert - usar accesores de Record (sin get)
        assertNotNull(result);
        assertEquals("Jose Lema", result.nombre());
        assertEquals("CLI001", result.clienteId());
        assertEquals("1234567890", result.identificacion());

        verify(clienteRepository, times(1)).existsByClienteId(anyString());
        verify(clienteRepository, times(1)).existsByIdentificacion(anyString());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(messagePublisher, times(1)).publishClienteEvent(any());
    }

    @Test
    @DisplayName("Lanzar excepción cuando clienteId ya existe")
    void testCrearClienteConClienteIdExistente() {
        // Arrange
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(true);

        // Act & Assert - usar método crear() en lugar de crearCliente()
        assertThrows(ClienteAlreadyExistsException.class, () ->
            clienteService.crear(requestDto)
        );

        verify(clienteRepository, times(1)).existsByClienteId(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando identificación ya existe")
    void testCrearClienteConIdentificacionExistente() {
        // Arrange
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(false);
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(true);

        // Act & Assert - usar método crear() en lugar de crearCliente()
        assertThrows(ClienteAlreadyExistsException.class, () ->
            clienteService.crear(requestDto)
        );

        verify(clienteRepository, times(1)).existsByIdentificacion(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Obtener todos los clientes exitosamente")
    void testObtenerTodosLosClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(responseDto);

        // Act - usar método obtenerTodos() en lugar de obtenerTodosLosClientes()
        List<ClienteResponseDto> result = clienteService.obtenerTodos();

        // Assert - usar accesores de Record (sin get)
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jose Lema", result.get(0).nombre());

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener cliente por ID exitosamente")
    void testObtenerClientePorId() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(responseDto);

        // Act - usar método obtenerPorId() en lugar de obtenerClientePorId()
        ClienteResponseDto result = clienteService.obtenerPorId(1L);

        // Assert - usar accesores de Record (sin get)
        assertNotNull(result);
        assertEquals("Jose Lema", result.nombre());
        assertEquals(1L, result.id());

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Lanzar excepción cuando cliente no existe por ID")
    void testObtenerClientePorIdNoExistente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert - usar método obtenerPorId() en lugar de obtenerClientePorId()
        assertThrows(ClienteNotFoundException.class, () ->
            clienteService.obtenerPorId(1L)
        );

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Actualizar cliente exitosamente")
    void testActualizarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(responseDto);

        // Act - usar método actualizar() en lugar de actualizarCliente()
        ClienteResponseDto result = clienteService.actualizar(1L, requestDto);

        // Assert - usar accesores de Record (sin get)
        assertNotNull(result);
        assertEquals("Jose Lema", result.nombre());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(messagePublisher, times(1)).publishClienteEvent(any());
    }

    @Test
    @DisplayName("Eliminar cliente exitosamente")
    void testEliminarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).deleteById(1L);

        // Act - usar método eliminar() en lugar de eliminarCliente()
        clienteService.eliminar(1L);

        // Assert
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
        verify(messagePublisher, times(1)).publishClienteEvent(any());
    }

    @Test
    @DisplayName("Obtener cliente por clienteId exitosamente")
    void testObtenerClientePorClienteId() {
        // Arrange
        when(clienteRepository.findByClienteId("CLI001")).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(responseDto);

        // Act - usar método obtenerPorClienteId()
        ClienteResponseDto result = clienteService.obtenerPorClienteId("CLI001");

        // Assert - usar accesores de Record (sin get)
        assertNotNull(result);
        assertEquals("CLI001", result.clienteId());

        verify(clienteRepository, times(1)).findByClienteId("CLI001");
    }
}

