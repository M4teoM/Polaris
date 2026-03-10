package com.polaris.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String nombre;

    @Column(length = 300, nullable = false)
    private String descripcion;

    @Column(length = 1000)
    private String descripcionDetallada;

    @Column(nullable = false)
    private Double precio;

    @Column(length = 300)
    private String imagenUrl;

    @Column(length = 50, nullable = false)
    private String categoria;

    public Servicio() {}

    public Servicio(Long id, String nombre, String descripcion, Double precio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.descripcionDetallada = descripcion;
        this.categoria = "Servicio";
    }

    public Servicio(Long id, String nombre, String descripcion, String descripcionDetallada,
                    Double precio, String imagenUrl, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descripcionDetallada = descripcionDetallada;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
    }

    public Servicio(String nombre, String descripcion, String descripcionDetallada,
                    Double precio, String imagenUrl, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descripcionDetallada = descripcionDetallada;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcionDetallada() { return descripcionDetallada; }
    public void setDescripcionDetallada(String descripcionDetallada) { this.descripcionDetallada = descripcionDetallada; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}