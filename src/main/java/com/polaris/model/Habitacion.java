package com.polaris.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin")
    private Administrador administrador;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<ReservaHabitacion> reservas;



    public Habitacion(String numero, int piso, String estado, TipoHabitacion tipoHabitacion) {
        this.numero = numero;
        this.piso = piso;
        this.estado = estado;
        this.tipoHabitacion = tipoHabitacion;
    }

   
    // Helper para formularios
    public Long getTipoHabitacionId() {
        return tipoHabitacion != null ? tipoHabitacion.getId() : null;
    }
}