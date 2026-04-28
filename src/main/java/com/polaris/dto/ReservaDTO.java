package com.polaris.dto;

import java.time.LocalDate;

/**
 * DTO plano de reserva para el Portal de Usuario.
 *
 * Incluye solo valores primitivos/planos para evitar exponer objetos
 * anidados como Cliente o Habitacion dentro de la respuesta REST.
 */
public class ReservaDTO {

    private Long id;
    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    private String estado;
    private int numeroHuespedes;
    private Long clienteId;
    private String clienteNombreCompleto;
    private Long habitacionId;
    private String habitacionNumero;
    private Long tipoHabitacionId;
    private String tipoHabitacionNombre;

    public ReservaDTO() {
    }

    public ReservaDTO(Long id,
                      LocalDate fechaCheckIn,
                      LocalDate fechaCheckOut,
                      String estado,
                      int numeroHuespedes,
                      Long clienteId,
                      String clienteNombreCompleto,
                      Long habitacionId,
                      String habitacionNumero,
                      Long tipoHabitacionId,
                      String tipoHabitacionNombre) {
        this.id = id;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.estado = estado;
        this.numeroHuespedes = numeroHuespedes;
        this.clienteId = clienteId;
        this.clienteNombreCompleto = clienteNombreCompleto;
        this.habitacionId = habitacionId;
        this.habitacionNumero = habitacionNumero;
        this.tipoHabitacionId = tipoHabitacionId;
        this.tipoHabitacionNombre = tipoHabitacionNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getNumeroHuespedes() {
        return numeroHuespedes;
    }

    public void setNumeroHuespedes(int numeroHuespedes) {
        this.numeroHuespedes = numeroHuespedes;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombreCompleto() {
        return clienteNombreCompleto;
    }

    public void setClienteNombreCompleto(String clienteNombreCompleto) {
        this.clienteNombreCompleto = clienteNombreCompleto;
    }

    public Long getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(Long habitacionId) {
        this.habitacionId = habitacionId;
    }

    public String getHabitacionNumero() {
        return habitacionNumero;
    }

    public void setHabitacionNumero(String habitacionNumero) {
        this.habitacionNumero = habitacionNumero;
    }

    public Long getTipoHabitacionId() {
        return tipoHabitacionId;
    }

    public void setTipoHabitacionId(Long tipoHabitacionId) {
        this.tipoHabitacionId = tipoHabitacionId;
    }

    public String getTipoHabitacionNombre() {
        return tipoHabitacionNombre;
    }

    public void setTipoHabitacionNombre(String tipoHabitacionNombre) {
        this.tipoHabitacionNombre = tipoHabitacionNombre;
    }
}
