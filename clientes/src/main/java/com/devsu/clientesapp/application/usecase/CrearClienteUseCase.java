package com.devsu.clientesapp.application.usecase;

import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;

/**
 * Caso de uso para crear un nuevo cliente en el sistema
 */
public interface CrearClienteUseCase {
    ClienteResponseDto crear(ClienteRequestDto request);
}

