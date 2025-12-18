package com.devsu.clientesapp.domain.exception;

public class ClienteAlreadyExistsException extends RuntimeException {

    public ClienteAlreadyExistsException(String message) {
        super(message);
    }

    public ClienteAlreadyExistsException(String field, String value) {
        super(String.format("Ya existe un cliente con %s: %s", field, value));
    }
}

