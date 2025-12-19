package com.devsu.clientesapp.application.service;

import com.devsu.clientesapp.application.dto.ClienteConCuentasRequestDto;
import com.devsu.clientesapp.application.dto.ClienteEventDto;
import com.devsu.clientesapp.application.dto.ClienteRequestDto;
import com.devsu.clientesapp.application.dto.ClienteResponseDto;
import com.devsu.clientesapp.application.dto.CuentaInfoDto;
import com.devsu.clientesapp.application.mapper.ClienteMapper;
import com.devsu.clientesapp.application.usecase.*;
import com.devsu.clientesapp.domain.exception.ClienteAlreadyExistsException;
import com.devsu.clientesapp.domain.exception.ClienteNotFoundException;
import com.devsu.clientesapp.domain.model.Cliente;
import com.devsu.clientesapp.domain.repository.ClienteRepository;
import com.devsu.clientesapp.infrastructure.messaging.ClienteMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService implements
        CrearClienteUseCase,
        CrearClienteConCuentasUseCase,
        ObtenerClientesUseCase,
        ObtenerClientePorIdUseCase,
        ObtenerClientePorClienteIdUseCase,
        ActualizarClienteUseCase,
        EliminarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteMessagePublisher messagePublisher;

    @Override
    @Transactional
    public ClienteResponseDto crear(ClienteRequestDto requestDto) {
        log.info("Creando cliente con clienteId: {}", requestDto.clienteId());

        // Validar que no exista cliente con el mismo clienteId
        if (clienteRepository.existsByClienteId(requestDto.clienteId())) {
            throw new ClienteAlreadyExistsException("clienteId", requestDto.clienteId());
        }

        // Validar que no exista cliente con la misma identificación
        if (clienteRepository.existsByIdentificacion(requestDto.identificacion())) {
            throw new ClienteAlreadyExistsException("identificacion", requestDto.identificacion());
        }

        Cliente cliente = clienteMapper.toEntity(requestDto);
        Cliente savedCliente = clienteRepository.save(cliente);

        // Publicar evento de creación
        ClienteEventDto event = new ClienteEventDto(
                savedCliente.getClienteId(),
                savedCliente.getNombre(),
                savedCliente.getIdentificacion(),
                "CREATED"
        );
        messagePublisher.publishClienteEvent(event);

        log.info("Cliente creado exitosamente con id: {}", savedCliente.getId());
        return clienteMapper.toDto(savedCliente);
    }

    @Override
    @Transactional
    public ClienteResponseDto crear(ClienteConCuentasRequestDto requestDto) {
        log.info("Creando cliente con cuentas: {}", requestDto.getCliente().nombre());

        // Validar que no exista el cliente
        if (clienteRepository.existsByClienteId(requestDto.getCliente().clienteId())) {
            throw new ClienteAlreadyExistsException("clienteId", requestDto.getCliente().clienteId());
        }

        if (clienteRepository.existsByIdentificacion(requestDto.getCliente().identificacion())) {
            throw new ClienteAlreadyExistsException("identificacion", requestDto.getCliente().identificacion());
        }

        // Crear el cliente
        Cliente cliente = clienteMapper.toEntity(requestDto.getCliente());
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente: {}", clienteGuardado.getClienteId());

        // Publicar evento con las cuentas
        List<CuentaInfoDto> cuentas = requestDto.getCuentas();
        ClienteEventDto event = new ClienteEventDto(
                clienteGuardado.getClienteId(),
                clienteGuardado.getNombre(),
                clienteGuardado.getIdentificacion(),
                "CREATED",
                cuentas
        );

        messagePublisher.publishClienteEvent(event);
        log.info("Evento CREATED publicado con {} cuenta(s) para el cliente: {}",
                cuentas.size(), clienteGuardado.getClienteId());

        return clienteMapper.toDto(clienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDto> obtenerTodos() {
        log.info("Obteniendo todos los clientes");
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorId(Long id) {
        log.info("Obteniendo cliente por id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorClienteId(String clienteId) {
        log.info("Obteniendo cliente por clienteId: {}", clienteId);
        Cliente cliente = clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("clienteId", clienteId));
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDto actualizar(Long id, ClienteRequestDto requestDto) {
        log.info("Actualizando cliente con id: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        // Validar identificación única si se está actualizando
        if (requestDto.identificacion() != null &&
            !requestDto.identificacion().equals(cliente.getIdentificacion())) {
            if (clienteRepository.existsByIdentificacion(requestDto.identificacion())) {
                throw new ClienteAlreadyExistsException("identificacion", requestDto.identificacion());
            }
        }

        clienteMapper.updateEntityFromDto(requestDto, cliente);
        Cliente updatedCliente = clienteRepository.save(cliente);

        // Publicar evento de actualización
        ClienteEventDto event = new ClienteEventDto(
                updatedCliente.getClienteId(),
                updatedCliente.getNombre(),
                updatedCliente.getIdentificacion(),
                "UPDATED"
        );
        messagePublisher.publishClienteEvent(event);

        log.info("Cliente actualizado exitosamente con id: {}", id);
        return clienteMapper.toDto(updatedCliente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando cliente con id: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        String clienteId = cliente.getClienteId();
        clienteRepository.deleteById(id);

        // Publicar evento de eliminación
        ClienteEventDto event = new ClienteEventDto(
                clienteId,
                null,
                null,
                "DELETED"
        );
        messagePublisher.publishClienteEvent(event);

        log.info("Cliente eliminado exitosamente con id: {}", id);
    }
}

