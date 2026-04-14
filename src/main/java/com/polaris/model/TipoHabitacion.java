package com.polaris.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_habitacion")
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(length = 500, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double precioPorNoche;

    @Column(length = 300)
    private String imagenUrl;

    @Column(nullable = false)
    private int metrosCuadrados;

    @Column(nullable = false)
    private int capacidad;

    @Column(length = 30, nullable = false)
    private String tipoCama;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoHabitacion", fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones;


    public TipoHabitacion(String nombre, String descripcion,
                          Double precioPorNoche, String imagenUrl,
                          int metrosCuadrados, int capacidad, String tipoCama) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioPorNoche = precioPorNoche;
        this.imagenUrl = imagenUrl;
        this.metrosCuadrados = metrosCuadrados;
        this.capacidad = capacidad;
        this.tipoCama = tipoCama;
    }

  
}