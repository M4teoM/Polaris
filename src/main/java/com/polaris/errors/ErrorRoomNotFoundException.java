package com.polaris.errors;

public class ErrorRoomNotFoundException extends RuntimeException {
    public ErrorRoomNotFoundException(Long id) {
        super("Habitación con ID " + id + " no encontrada.");
    }
}