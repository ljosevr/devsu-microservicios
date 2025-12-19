package com.devsu.clientesapp.application.usecase;

import com.devsu.clientesapp.application.dto.ClienteConCuentasRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;

/**
 * Caso de uso para crear un cliente con sus cuentas asociadas
 * mediante comunicación asíncrona con el microservicio de cuentas
 */
public interface CrearClienteConCuentasUseCase {
    ClienteResponseDto crear(ClienteConCuentasRequestDto request);
}
