package com.devsu.clientesapp.application.usecase;

import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;

/**
 * Caso de uso para actualizar los datos de un cliente existente
 */
public interface ActualizarClienteUseCase {
    ClienteResponseDto actualizar(Long id, ClienteRequestDto request);
}

