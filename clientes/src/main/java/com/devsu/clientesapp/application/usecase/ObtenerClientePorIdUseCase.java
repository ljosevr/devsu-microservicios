package com.devsu.clientesapp.application.usecase;

import com.devsu.clientesapp.application.dto.ClienteResponseDto;

/**
 * Caso de uso para obtener un cliente por su ID
 */
public interface ObtenerClientePorIdUseCase {
    ClienteResponseDto obtenerPorId(Long id);
}

