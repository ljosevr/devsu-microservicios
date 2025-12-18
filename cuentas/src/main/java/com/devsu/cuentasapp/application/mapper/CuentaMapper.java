package com.devsu.cuentasapp.application.mapper;

import com.devsu.cuentasapp.application.dto.CuentaRequestDto;
import com.devsu.cuentasapp.application.dto.CuentaResponseDto;
import com.devsu.cuentasapp.domain.model.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toEntity(CuentaRequestDto dto) {
        return Cuenta.builder()
                .numeroCuenta(dto.getNumeroCuenta())
                .tipoCuenta(dto.getTipoCuenta().toUpperCase())
                .saldoInicial(dto.getSaldoInicial())
                .saldoActual(dto.getSaldoInicial())
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .clienteId(dto.getClienteId())
                .build();
    }

    public CuentaResponseDto toDto(Cuenta entity) {
        return CuentaResponseDto.builder()
                .id(entity.getId())
                .numeroCuenta(entity.getNumeroCuenta())
                .tipoCuenta(entity.getTipoCuenta())
                .saldoInicial(entity.getSaldoInicial())
                .saldoActual(entity.getSaldoActual())
                .estado(entity.getEstado())
                .clienteId(entity.getClienteId())
                .clienteNombre(entity.getClienteNombre())
                .build();
    }

    public void updateEntityFromDto(CuentaRequestDto dto, Cuenta entity) {
        if (dto.getTipoCuenta() != null) {
            entity.setTipoCuenta(dto.getTipoCuenta().toUpperCase());
        }
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
    }
}


