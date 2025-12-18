package com.devsu.cuentasapp.application.service;

import com.devsu.cuentasapp.application.dto.CuentaRequestDto;
import com.devsu.cuentasapp.application.dto.CuentaResponseDto;
import com.devsu.cuentasapp.application.mapper.CuentaMapper;
import com.devsu.cuentasapp.domain.exception.CuentaAlreadyExistsException;
import com.devsu.cuentasapp.domain.exception.CuentaNotFoundException;
import com.devsu.cuentasapp.domain.model.Cuenta;
import com.devsu.cuentasapp.domain.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CuentaService")
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @InjectMocks
    private CuentaService cuentaService;

    private CuentaRequestDto requestDto;
    private Cuenta cuenta;
    private CuentaResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = CuentaRequestDto.builder()
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId("CLI001")
                .build();

        cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .saldoActual(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId("CLI001")
                .clienteNombre("Jose Lema")
                .build();

        responseDto = CuentaResponseDto.builder()
                .id(1L)
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .saldoActual(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId("CLI001")
                .clienteNombre("Jose Lema")
                .build();
    }

    @Test
    @DisplayName("Crear cuenta exitosamente")
    void testCrearCuentaExitosamente() {
        // Arrange
        when(cuentaRepository.existsByNumeroCuenta(anyString())).thenReturn(false);
        when(cuentaMapper.toEntity(any(CuentaRequestDto.class))).thenReturn(cuenta);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(responseDto);

        // Act
        CuentaResponseDto result = cuentaService.crearCuenta(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("123456", result.getNumeroCuenta());
        assertEquals("AHORROS", result.getTipoCuenta());
        assertEquals(new BigDecimal("1000.00"), result.getSaldoInicial());

        verify(cuentaRepository, times(1)).existsByNumeroCuenta(anyString());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando número de cuenta ya existe")
    void testCrearCuentaConNumeroExistente() {
        // Arrange
        when(cuentaRepository.existsByNumeroCuenta(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(CuentaAlreadyExistsException.class, () -> {
            cuentaService.crearCuenta(requestDto);
        });

        verify(cuentaRepository, times(1)).existsByNumeroCuenta(anyString());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    @DisplayName("Obtener todas las cuentas exitosamente")
    void testObtenerTodasLasCuentas() {
        // Arrange
        List<Cuenta> cuentas = Arrays.asList(cuenta);
        when(cuentaRepository.findAll()).thenReturn(cuentas);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(responseDto);

        // Act
        List<CuentaResponseDto> result = cuentaService.obtenerTodasLasCuentas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123456", result.get(0).getNumeroCuenta());

        verify(cuentaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener cuenta por ID exitosamente")
    void testObtenerCuentaPorId() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(responseDto);

        // Act
        CuentaResponseDto result = cuentaService.obtenerCuentaPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123456", result.getNumeroCuenta());

        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Lanzar excepción cuando cuenta no existe por ID")
    void testObtenerCuentaPorIdNoExistente() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CuentaNotFoundException.class, () -> {
            cuentaService.obtenerCuentaPorId(1L);
        });

        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener cuenta por número exitosamente")
    void testObtenerCuentaPorNumero() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(responseDto);

        // Act
        CuentaResponseDto result = cuentaService.obtenerCuentaPorNumero("123456");

        // Assert
        assertNotNull(result);
        assertEquals("123456", result.getNumeroCuenta());

        verify(cuentaRepository, times(1)).findByNumeroCuenta("123456");
    }

    @Test
    @DisplayName("Obtener cuentas por clienteId")
    void testObtenerCuentasPorCliente() {
        // Arrange
        List<Cuenta> cuentas = Arrays.asList(cuenta);
        when(cuentaRepository.findByClienteId("CLI001")).thenReturn(cuentas);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(responseDto);

        // Act
        List<CuentaResponseDto> result = cuentaService.obtenerCuentasPorCliente("CLI001");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CLI001", result.get(0).getClienteId());

        verify(cuentaRepository, times(1)).findByClienteId("CLI001");
    }

    @Test
    @DisplayName("Actualizar cuenta exitosamente")
    void testActualizarCuenta() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(responseDto);
        doNothing().when(cuentaMapper).updateEntityFromDto(any(), any());

        // Act
        CuentaResponseDto result = cuentaService.actualizarCuenta(1L, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("123456", result.getNumeroCuenta());

        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }
}

