package com.devsu.cuentasapp.infrastructure.rest;

import com.devsu.cuentasapp.application.dto.EstadoCuentaDto;
import com.devsu.cuentasapp.application.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "API para generación de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    @Operation(summary = "Generar estado de cuenta por cliente y rango de fechas")
    public ResponseEntity<EstadoCuentaDto> generarEstadoCuenta(
            @Parameter(description = "ID del cliente", required = true)
            @RequestParam String cliente,

            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,

            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        EstadoCuentaDto estadoCuenta = reporteService.generarEstadoCuenta(cliente, fechaInicio, fechaFin);
        return ResponseEntity.ok(estadoCuenta);
    }
}

