package com.devsu.clientesapp.application.usecase;

import com.devsu.clientesapp.application.dto.ClienteResponseDto;

import java.util.List;

/**
 * Caso de uso para obtener todos los clientes del sistema
 */
public interface ObtenerClientesUseCase {
    List<ClienteResponseDto> obtenerTodos();
}

