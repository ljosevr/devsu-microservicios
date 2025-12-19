package com.devsu.clientesapp.infrastructure.rest;

import com.devsu.clientesapp.application.dto.ClienteConCuentasRequestDto;
import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;
import com.devsu.clientesapp.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    private final CrearClienteUseCase crearClienteUseCase;
    private final CrearClienteConCuentasUseCase crearClienteConCuentasUseCase;
    private final ObtenerClientesUseCase obtenerClientesUseCase;
    private final ObtenerClientePorIdUseCase obtenerClientePorIdUseCase;
    private final ObtenerClientePorClienteIdUseCase obtenerClientePorClienteIdUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final EliminarClienteUseCase eliminarClienteUseCase;

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    public ResponseEntity<ClienteResponseDto> crearCliente(@Valid @RequestBody ClienteRequestDto requestDto) {
        ClienteResponseDto response = crearClienteUseCase.crear(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/con-cuentas")
    @Operation(summary = "Crear un nuevo cliente con sus cuentas asociadas",
               description = "Crea un cliente y automáticamente crea sus cuentas en el microservicio de cuentas mediante eventos")
    public ResponseEntity<ClienteResponseDto> crearClienteConCuentas(@Valid @RequestBody ClienteConCuentasRequestDto requestDto) {
        ClienteResponseDto response = crearClienteConCuentasUseCase.crear(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public ResponseEntity<List<ClienteResponseDto>> obtenerTodosLosClientes() {
        List<ClienteResponseDto> clientes = obtenerClientesUseCase.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorId(@PathVariable Long id) {
        ClienteResponseDto cliente = obtenerClientePorIdUseCase.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/clienteId/{clienteId}")
    @Operation(summary = "Obtener cliente por clienteId")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorClienteId(@PathVariable String clienteId) {
        ClienteResponseDto cliente = obtenerClientePorClienteIdUseCase.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente")
    public ResponseEntity<ClienteResponseDto> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDto requestDto) {
        ClienteResponseDto response = actualizarClienteUseCase.actualizar(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un cliente")
    public ResponseEntity<ClienteResponseDto> actualizarParcialmenteCliente(
            @PathVariable Long id,
            @RequestBody ClienteRequestDto requestDto) {
        ClienteResponseDto response = actualizarClienteUseCase.actualizar(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        eliminarClienteUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

