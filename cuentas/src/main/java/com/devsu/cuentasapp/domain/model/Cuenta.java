package com.devsu.cuentasapp.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false, length = 20)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 50)
    private String tipoCuenta; // AHORROS, CORRIENTE

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "cliente_nombre")
    private String clienteNombre;

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = true;
        }
        if (saldoActual == null) {
            saldoActual = saldoInicial;
        }
    }
}

