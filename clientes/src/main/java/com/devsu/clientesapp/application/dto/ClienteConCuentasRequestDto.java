package com.devsu.clientesapp.application.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
@Data
public class ClienteConCuentasRequestDto {
    @Valid
    @NotNull(message = "La información del cliente es requerida")
    private ClienteRequestDto cliente;
    @Valid
    @NotEmpty(message = "Debe incluir al menos una cuenta")
    private List<CuentaInfoDto> cuentas;
}
