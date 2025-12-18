package com.devsu.cuentasapp.infrastructure.rest;

import com.devsu.cuentasapp.application.dto.CuentaRequestDto;
import com.devsu.cuentasapp.application.dto.CuentaResponseDto;
import com.devsu.cuentasapp.application.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "API para gestión de cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    @Operation(summary = "Crear una nueva cuenta")
    public ResponseEntity<CuentaResponseDto> crearCuenta(@Valid @RequestBody CuentaRequestDto requestDto) {
        CuentaResponseDto response = cuentaService.crearCuenta(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las cuentas")
    public ResponseEntity<List<CuentaResponseDto>> obtenerTodasLasCuentas() {
        List<CuentaResponseDto> cuentas = cuentaService.obtenerTodasLasCuentas();
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por ID")
    public ResponseEntity<CuentaResponseDto> obtenerCuentaPorId(@PathVariable Long id) {
        CuentaResponseDto cuenta = cuentaService.obtenerCuentaPorId(id);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/numero/{numeroCuenta}")
    @Operation(summary = "Obtener cuenta por número")
    public ResponseEntity<CuentaResponseDto> obtenerCuentaPorNumero(@PathVariable String numeroCuenta) {
        CuentaResponseDto cuenta = cuentaService.obtenerCuentaPorNumero(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener cuentas por cliente")
    public ResponseEntity<List<CuentaResponseDto>> obtenerCuentasPorCliente(@PathVariable String clienteId) {
        List<CuentaResponseDto> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId);
        return ResponseEntity.ok(cuentas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cuenta")
    public ResponseEntity<CuentaResponseDto> actualizarCuenta(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequestDto requestDto) {
        CuentaResponseDto response = cuentaService.actualizarCuenta(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una cuenta")
    public ResponseEntity<CuentaResponseDto> actualizarParcialmenteCuenta(
            @PathVariable Long id,
            @RequestBody CuentaRequestDto requestDto) {
        CuentaResponseDto response = cuentaService.actualizarCuenta(id, requestDto);
        return ResponseEntity.ok(response);
    }
}


