package com.polaris.dto;

import java.time.LocalDate;

/**
 * DTO de entrada para actualizar una reserva desde el Portal de Usuario.
 * Solo incluye los campos que el cliente puede modificar:
 * fechas de check-in/check-out y número de huéspedes.
 * El estado y la habitación no se pueden cambiar desde este DTO.
 */
public class ActualizarReservaDTO {

    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;

    // Integer (con mayúscula) para que pueda ser null si no se envía el campo
    private Integer numeroHuespedes;

    // Constructor vacío requerido por Jackson para deserializar el body del request
    public ActualizarReservaDTO() {}

    // ── Getters y Setters ────────────────────────────────────────────────

    public LocalDate getFechaCheckIn() { return fechaCheckIn; }
    public void setFechaCheckIn(LocalDate fechaCheckIn) { this.fechaCheckIn = fechaCheckIn; }

    public LocalDate getFechaCheckOut() { return fechaCheckOut; }
    public void setFechaCheckOut(LocalDate fechaCheckOut) { this.fechaCheckOut = fechaCheckOut; }

    public Integer getNumeroHuespedes() { return numeroHuespedes; }
    public void setNumeroHuespedes(Integer numeroHuespedes) { this.numeroHuespedes = numeroHuespedes; }
}