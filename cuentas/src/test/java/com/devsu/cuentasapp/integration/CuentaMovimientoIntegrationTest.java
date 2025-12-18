package com.devsu.cuentasapp.integration;

import com.devsu.cuentasapp.application.dto.CuentaRequestDto;
import com.devsu.cuentasapp.application.dto.CuentaResponseDto;
import com.devsu.cuentasapp.application.dto.MovimientoRequestDto;
import com.devsu.cuentasapp.application.dto.MovimientoResponseDto;
import com.devsu.cuentasapp.domain.model.Cuenta;
import com.devsu.cuentasapp.domain.repository.CuentaRepository;
import com.devsu.cuentasapp.domain.repository.MovimientoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración - Cuentas y Movimientos")
class CuentaMovimientoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @BeforeEach
    void setUp() {
        // Eliminar en orden: primero movimientos, luego cuentas (por integridad referencial)
        movimientoRepository.deleteAll();
        cuentaRepository.deleteAll();
    }

    @Test
    @DisplayName("Flujo completo: Crear cuenta, realizar depósito y retiro")
    void testFlujoCompletoContaMovimientos() throws Exception {
        // 1. Crear una cuenta
        CuentaRequestDto cuentaRequest = CuentaRequestDto.builder()
                .numeroCuenta("123456")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId("CLI001")
                .build();

        MvcResult cuentaResult = mockMvc.perform(post("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("123456"))
                .andExpect(jsonPath("$.saldoActual").value(1000.00))
                .andReturn();

        CuentaResponseDto cuentaResponse = objectMapper.readValue(
                cuentaResult.getResponse().getContentAsString(),
                CuentaResponseDto.class
        );

        assertThat(cuentaResponse.getId()).isNotNull();
        assertThat(cuentaResponse.getSaldoActual()).isEqualByComparingTo(new BigDecimal("1000.00"));

        // 2. Realizar un depósito
        MovimientoRequestDto depositoRequest = MovimientoRequestDto.builder()
                .numeroCuenta("123456")
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("500.00"))
                .build();

        MvcResult depositoResult = mockMvc.perform(post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.valor").value(500.00))
                .andExpect(jsonPath("$.saldo").value(1500.00))
                .andReturn();

        MovimientoResponseDto depositoResponse = objectMapper.readValue(
                depositoResult.getResponse().getContentAsString(),
                MovimientoResponseDto.class
        );

        assertThat(depositoResponse.getSaldo()).isEqualByComparingTo(new BigDecimal("1500.00"));

        // 3. Verificar que el saldo de la cuenta se actualizó
        mockMvc.perform(get("/cuentas/numero/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoActual").value(1500.00));

        // 4. Realizar un retiro
        MovimientoRequestDto retiroRequest = MovimientoRequestDto.builder()
                .numeroCuenta("123456")
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("300.00"))
                .build();

        MvcResult retiroResult = mockMvc.perform(post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(retiroRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoMovimiento").value("RETIRO"))
                .andExpect(jsonPath("$.saldo").value(1200.00))
                .andReturn();

        MovimientoResponseDto retiroResponse = objectMapper.readValue(
                retiroResult.getResponse().getContentAsString(),
                MovimientoResponseDto.class
        );

        assertThat(retiroResponse.getSaldo()).isEqualByComparingTo(new BigDecimal("1200.00"));

        // 5. Verificar saldo final
        Cuenta cuentaFinal = cuentaRepository.findByNumeroCuenta("123456").orElseThrow();
        assertThat(cuentaFinal.getSaldoActual()).isEqualByComparingTo(new BigDecimal("1200.00"));

        // 6. Intentar retiro con saldo insuficiente
        MovimientoRequestDto retiroExcesivoRequest = MovimientoRequestDto.builder()
                .numeroCuenta("123456")
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("2000.00"))
                .build();

        mockMvc.perform(post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(retiroExcesivoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    @DisplayName("Obtener movimientos por cuenta")
    void testObtenerMovimientosPorCuenta() throws Exception {
        // Crear cuenta
        CuentaRequestDto cuentaRequest = CuentaRequestDto.builder()
                .numeroCuenta("789012")
                .tipoCuenta("CORRIENTE")
                .saldoInicial(new BigDecimal("500.00"))
                .estado(true)
                .clienteId("CLI002")
                .build();

        mockMvc.perform(post("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaRequest)))
                .andExpect(status().isCreated());

        // Realizar varios movimientos
        MovimientoRequestDto mov1 = MovimientoRequestDto.builder()
                .numeroCuenta("789012")
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("100.00"))
                .build();

        MovimientoRequestDto mov2 = MovimientoRequestDto.builder()
                .numeroCuenta("789012")
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("50.00"))
                .build();

        mockMvc.perform(post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mov1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mov2)))
                .andExpect(status().isCreated());

        // Obtener todos los movimientos de la cuenta
        mockMvc.perform(get("/movimientos/cuenta/789012"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

