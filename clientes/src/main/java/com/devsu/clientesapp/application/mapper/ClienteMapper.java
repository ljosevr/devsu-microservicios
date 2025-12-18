package com.devsu.clientesapp.application.mapper;

import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;
import com.devsu.clientesapp.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDto dto) {
        return Cliente.builder()
                .nombre(dto.nombre())
                .genero(dto.genero())
                .edad(dto.edad())
                .identificacion(dto.identificacion())
                .direccion(dto.direccion())
                .telefono(dto.telefono())
                .clienteId(dto.clienteId())
                .contrasena(dto.contrasena())
                .estado(dto.estado() != null ? dto.estado() : true)
                .build();
    }

    public ClienteResponseDto toDto(Cliente entity) {
        return new ClienteResponseDto(
                entity.getId(),
                entity.getNombre(),
                entity.getGenero(),
                entity.getEdad(),
                entity.getIdentificacion(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getClienteId(),
                entity.getEstado()
        );
    }

    public void updateEntityFromDto(ClienteRequestDto dto, Cliente entity) {
        if (dto.nombre() != null) {
            entity.setNombre(dto.nombre());
        }
        if (dto.genero() != null) {
            entity.setGenero(dto.genero());
        }
        if (dto.edad() != null) {
            entity.setEdad(dto.edad());
        }
        if (dto.identificacion() != null) {
            entity.setIdentificacion(dto.identificacion());
        }
        if (dto.direccion() != null) {
            entity.setDireccion(dto.direccion());
        }
        if (dto.telefono() != null) {
            entity.setTelefono(dto.telefono());
        }
        if (dto.contrasena() != null) {
            entity.setContrasena(dto.contrasena());
        }
        if (dto.estado() != null) {
            entity.setEstado(dto.estado());
        }
    }
}

