package com.polaris.model;

public class Servicio {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagenUrl;

    public Servicio(Long id, String nombre, String descripcion, Double precio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }
}