package com.devsu.cuentasapp.domain.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException(String message) {
        super(message);
    }

    public CuentaNotFoundException(Long id) {
        super("Cuenta no encontrada con id: " + id);
    }

    public CuentaNotFoundException(String field, String value) {
        super(String.format("Cuenta no encontrada con %s: %s", field, value));
    }
}

