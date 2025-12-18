package com.devsu.clientesapp.application.usecase;

import com.devsu.clientesapp.application.dto.ClienteResponseDto;

/**
 * Caso de uso para obtener un cliente por su clienteId
 */
public interface ObtenerClientePorClienteIdUseCase {
    ClienteResponseDto obtenerPorClienteId(String clienteId);
}

