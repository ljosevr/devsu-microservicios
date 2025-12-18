package com.devsu.cuentasapp.application.mapper;

import com.devsu.cuentasapp.application.dto.MovimientoResponseDto;
import com.devsu.cuentasapp.domain.model.Movimiento;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public MovimientoResponseDto toDto(Movimiento entity) {
        return MovimientoResponseDto.builder()
                .id(entity.getId())
                .fecha(entity.getFecha())
                .tipoMovimiento(entity.getTipoMovimiento())
                .valor(entity.getValor())
                .saldo(entity.getSaldo())
                .numeroCuenta(entity.getCuenta().getNumeroCuenta())
                .build();
    }
}

