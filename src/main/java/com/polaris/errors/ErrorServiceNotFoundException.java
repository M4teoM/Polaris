package com.polaris.errors;

public class ErrorServiceNotFoundException extends RuntimeException {
    public ErrorServiceNotFoundException(Long id) {
        super("Servicio con ID " + id + " no encontrado.");
    }
}