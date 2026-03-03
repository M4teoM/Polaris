package com.polaris.model;

public class Habitacion {

    private Long id;
    private String numero;       // Ej: "101", "205"
    private int piso;
    private String estado;       // Disponible, Ocupada, Mantenimiento
    private Long tipoHabitacionId;

    // Transient: se rellena en el controlador para las vistas
    private TipoHabitacion tipoHabitacion;

    public Habitacion() {}

    public Habitacion(Long id, String numero, int piso, String estado, Long tipoHabitacionId) {
        this.id = id;
        this.numero = numero;
        this.piso = piso;
        this.estado = estado;
        this.tipoHabitacionId = tipoHabitacionId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public int getPiso() { return piso; }
    public void setPiso(int piso) { this.piso = piso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getTipoHabitacionId() { return tipoHabitacionId; }
    public void setTipoHabitacionId(Long tipoHabitacionId) { this.tipoHabitacionId = tipoHabitacionId; }

    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
}
