package com.polaris.dto;

import java.time.LocalDate;

/**
 * DTO de entrada para actualizar una reserva desde el Portal de Usuario.
 *
 * Se concentra en los campos que el cliente puede modificar en este flujo:
 * fechas de check-in/check-out y, opcionalmente, número de huéspedes.
 */
public class ActualizarReservaDTO {

    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    private Integer numeroHuespedes;

    public ActualizarReservaDTO() {
    }

    public LocalDate getFechaCheckIn() {
        return fechaCheckIn;
    }

    public void setFechaCheckIn(LocalDate fechaCheckIn) {
        this.fechaCheckIn = fechaCheckIn;
    }

    public LocalDate getFechaCheckOut() {
        return fechaCheckOut;
    }

    public void setFechaCheckOut(LocalDate fechaCheckOut) {
        this.fechaCheckOut = fechaCheckOut;
    }

    public Integer getNumeroHuespedes() {
        return numeroHuespedes;
    }

    public void setNumeroHuespedes(Integer numeroHuespedes) {
        this.numeroHuespedes = numeroHuespedes;
    }
}
