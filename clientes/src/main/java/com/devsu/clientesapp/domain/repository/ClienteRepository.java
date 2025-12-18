package com.devsu.clientesapp.domain.repository;

import com.devsu.clientesapp.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByClienteId(String clienteId);

    Optional<Cliente> findByIdentificacion(String identificacion);

    boolean existsByClienteId(String clienteId);

    boolean existsByIdentificacion(String identificacion);
}

