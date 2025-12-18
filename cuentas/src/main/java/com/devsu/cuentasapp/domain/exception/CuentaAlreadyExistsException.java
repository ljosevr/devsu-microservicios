package com.devsu.cuentasapp.domain.exception;

public class CuentaAlreadyExistsException extends RuntimeException {

    public CuentaAlreadyExistsException(String message) {
        super(message);
    }
}

