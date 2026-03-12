package com.polaris.errors;

public class ErrorUserNotFoundException extends RuntimeException {
    public ErrorUserNotFoundException(Long id) {
        super("Cliente con ID " + id + " no encontrado.");
    }
    public ErrorUserNotFoundException(String correo) {
        super("Cliente con correo " + correo + " no encontrado.");
    }
}