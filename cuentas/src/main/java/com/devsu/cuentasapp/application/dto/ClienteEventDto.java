package com.devsu.cuentasapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteEventDto {

    private String clienteId;
    private String nombre;
    private String identificacion;
    private String eventType;
}

