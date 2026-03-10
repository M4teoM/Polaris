package com.polaris.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private int piso;

    @Column(length = 20, nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_habitacion_id", nullable = false)
    private TipoHabitacion tipoHabitacion;

    public Habitacion() {}

    public Habitacion(Long id, String numero, int piso, String estado, TipoHabitacion tipoHabitacion) {
        this.id = id;
        this.numero = numero;
        this.piso = piso;
        this.estado = estado;
        this.tipoHabitacion = tipoHabitacion;
    }

    public Habitacion(String numero, int piso, String estado, TipoHabitacion tipoHabitacion) {
        this.numero = numero;
        this.piso = piso;
        this.estado = estado;
        this.tipoHabitacion = tipoHabitacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public int getPiso() { return piso; }
    public void setPiso(int piso) { this.piso = piso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }

    // Helper para formularios
    public Long getTipoHabitacionId() {
        return tipoHabitacion != null ? tipoHabitacion.getId() : null;
    }
}