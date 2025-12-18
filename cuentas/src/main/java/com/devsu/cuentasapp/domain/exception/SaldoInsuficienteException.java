package com.devsu.cuentasapp.domain.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(String message) {
        super(message);
    }

    public SaldoInsuficienteException() {
        super("Saldo no disponible");
    }
}

