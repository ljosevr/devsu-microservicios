package com.devsu.cuentasapp.domain.repository;

import com.devsu.cuentasapp.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<Movimiento> findByCuentaIdAndFechaBetween(
            @Param("cuentaId") Long cuentaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.clienteId = :clienteId AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteIdAndFechaBetween(
            @Param("clienteId") String clienteId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}

