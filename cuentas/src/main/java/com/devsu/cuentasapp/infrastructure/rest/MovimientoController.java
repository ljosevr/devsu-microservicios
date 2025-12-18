package com.devsu.cuentasapp.infrastructure.rest;

import com.devsu.cuentasapp.application.dto.MovimientoRequestDto;
import com.devsu.cuentasapp.application.dto.MovimientoResponseDto;
import com.devsu.cuentasapp.application.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "API para gestión de movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo movimiento")
    public ResponseEntity<MovimientoResponseDto> registrarMovimiento(
            @Valid @RequestBody MovimientoRequestDto requestDto) {
        MovimientoResponseDto response = movimientoService.registrarMovimiento(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los movimientos")
    public ResponseEntity<List<MovimientoResponseDto>> obtenerTodosLosMovimientos() {
        List<MovimientoResponseDto> movimientos = movimientoService.obtenerTodosLosMovimientos();
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID")
    public ResponseEntity<MovimientoResponseDto> obtenerMovimientoPorId(@PathVariable Long id) {
        MovimientoResponseDto movimiento = movimientoService.obtenerMovimientoPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(summary = "Obtener movimientos por cuenta")
    public ResponseEntity<List<MovimientoResponseDto>> obtenerMovimientosPorCuenta(
            @PathVariable String numeroCuenta) {
        List<MovimientoResponseDto> movimientos = movimientoService.obtenerMovimientosPorCuenta(numeroCuenta);
        return ResponseEntity.ok(movimientos);
    }
}

