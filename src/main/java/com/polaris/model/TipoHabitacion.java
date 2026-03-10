package com.polaris.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "tipoHabitacion", fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones;

    public TipoHabitacion() {}

    public TipoHabitacion(Long id, String nombre, String descripcion,
                          Double precioPorNoche, String imagenUrl,
                          int metrosCuadrados, int capacidad, String tipoCama) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioPorNoche = precioPorNoche;
        this.imagenUrl = imagenUrl;
        this.metrosCuadrados = metrosCuadrados;
        this.capacidad = capacidad;
        this.tipoCama = tipoCama;
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecioPorNoche() { return precioPorNoche; }
    public void setPrecioPorNoche(Double precioPorNoche) { this.precioPorNoche = precioPorNoche; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(int metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getTipoCama() { return tipoCama; }
    public void setTipoCama(String tipoCama) { this.tipoCama = tipoCama; }

    public List<Habitacion> getHabitaciones() { return habitaciones; }
    public void setHabitaciones(List<Habitacion> habitaciones) { this.habitaciones = habitaciones; }
}