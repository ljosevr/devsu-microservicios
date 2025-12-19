package com.devsu.cuentasapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteEventDto {

    private String clienteId;
    private String nombre;
    private String identificacion;
    private String eventType;
    private List<CuentaInfoDto> cuentas; // Lista de cuentas a crear (solo para CREATED)
}

