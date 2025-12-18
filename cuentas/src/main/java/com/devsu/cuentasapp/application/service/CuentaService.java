package com.devsu.cuentasapp.application.service;

import com.devsu.cuentasapp.application.dto.CuentaRequestDto;
import com.devsu.cuentasapp.application.dto.CuentaResponseDto;
import com.devsu.cuentasapp.application.mapper.CuentaMapper;
import com.devsu.cuentasapp.domain.exception.CuentaAlreadyExistsException;
import com.devsu.cuentasapp.domain.exception.CuentaNotFoundException;
import com.devsu.cuentasapp.domain.model.Cuenta;
import com.devsu.cuentasapp.domain.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Transactional
    public CuentaResponseDto crearCuenta(CuentaRequestDto requestDto) {
        log.info("Creando cuenta con número: {}", requestDto.getNumeroCuenta());

        // Validar que no exista cuenta con el mismo número
        if (cuentaRepository.existsByNumeroCuenta(requestDto.getNumeroCuenta())) {
            throw new CuentaAlreadyExistsException(requestDto.getNumeroCuenta());
        }

        Cuenta cuenta = cuentaMapper.toEntity(requestDto);
        Cuenta savedCuenta = cuentaRepository.save(cuenta);

        log.info("Cuenta creada exitosamente con id: {}", savedCuenta.getId());
        return cuentaMapper.toDto(savedCuenta);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDto> obtenerTodasLasCuentas() {
        log.info("Obteniendo todas las cuentas");
        return cuentaRepository.findAll().stream()
                .map(cuentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuentaResponseDto obtenerCuentaPorId(Long id) {
        log.info("Obteniendo cuenta por id: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        return cuentaMapper.toDto(cuenta);
    }

    @Transactional(readOnly = true)
    public CuentaResponseDto obtenerCuentaPorNumero(String numeroCuenta) {
        log.info("Obteniendo cuenta por número: {}", numeroCuenta);
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("numeroCuenta", numeroCuenta));
        return cuentaMapper.toDto(cuenta);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDto> obtenerCuentasPorCliente(String clienteId) {
        log.info("Obteniendo cuentas por clienteId: {}", clienteId);
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(cuentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CuentaResponseDto actualizarCuenta(Long id, CuentaRequestDto requestDto) {
        log.info("Actualizando cuenta con id: {}", id);

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));

        cuentaMapper.updateEntityFromDto(requestDto, cuenta);
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);

        log.info("Cuenta actualizada exitosamente con id: {}", id);
        return cuentaMapper.toDto(updatedCuenta);
    }
}

