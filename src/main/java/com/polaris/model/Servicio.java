package com.polaris.model;

public class Servicio {

    private Long id;
    private String nombre;
    private String descripcion;
    private String descripcionDetallada;
    private Double precio;
    private String imagenUrl;
    private String categoria;

    // Constructor original — sigue funcionando para la entrega 1
    public Servicio(Long id, String nombre, String descripcion, Double precio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.descripcionDetallada = descripcion; // fallback
        this.categoria = "Servicio";
    }

    // Constructor completo — para el diseño con tarjetas
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

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDescripcionDetallada() { return descripcionDetallada; }
    public Double getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }
    public String getCategoria() { return categoria; }
}