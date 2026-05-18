package com.polaris.dto;

import java.time.LocalDate;

/**
 * DTO plano de reserva — evita exponer las entidades anidadas (Cliente,
 * Habitacion) directamente en las respuestas REST.
 * Solo contiene tipos primitivos y Strings para que el JSON sea limpio.
 */
public class ReservaDTO {

    private Long      id;
    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    private String    estado;
    private int       numeroHuespedes;

    // Del cliente — solo el id y nombre, sin exponer contraseña ni relaciones
    private Long   clienteId;
    private String clienteNombreCompleto;

    // De la habitación — solo el id y número de habitación
    private Long   habitacionId;
    private String habitacionNumero;

    // Del tipo de habitación — solo el id y nombre
    private Long   tipoHabitacionId;
    private String tipoHabitacionNombre;

    // Constructor vacío requerido por Jackson para deserializar JSON
    public ReservaDTO() {}

    // Constructor completo usado en los services para armar la respuesta
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
        this.id                    = id;
        this.fechaCheckIn          = fechaCheckIn;
        this.fechaCheckOut         = fechaCheckOut;
        this.estado                = estado;
        this.numeroHuespedes       = numeroHuespedes;
        this.clienteId             = clienteId;
        this.clienteNombreCompleto = clienteNombreCompleto;
        this.habitacionId          = habitacionId;
        this.habitacionNumero      = habitacionNumero;
        this.tipoHabitacionId      = tipoHabitacionId;
        this.tipoHabitacionNombre  = tipoHabitacionNombre;
    }

    // ── Getters y Setters ────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaCheckIn() { return fechaCheckIn; }
    public void setFechaCheckIn(LocalDate fechaCheckIn) { this.fechaCheckIn = fechaCheckIn; }

    public LocalDate getFechaCheckOut() { return fechaCheckOut; }
    public void setFechaCheckOut(LocalDate fechaCheckOut) { this.fechaCheckOut = fechaCheckOut; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getNumeroHuespedes() { return numeroHuespedes; }
    public void setNumeroHuespedes(int numeroHuespedes) { this.numeroHuespedes = numeroHuespedes; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombreCompleto() { return clienteNombreCompleto; }
    public void setClienteNombreCompleto(String n) { this.clienteNombreCompleto = n; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getHabitacionNumero() { return habitacionNumero; }
    public void setHabitacionNumero(String habitacionNumero) { this.habitacionNumero = habitacionNumero; }

    public Long getTipoHabitacionId() { return tipoHabitacionId; }
    public void setTipoHabitacionId(Long tipoHabitacionId) { this.tipoHabitacionId = tipoHabitacionId; }

    public String getTipoHabitacionNombre() { return tipoHabitacionNombre; }
    public void setTipoHabitacionNombre(String n) { this.tipoHabitacionNombre = n; }
}