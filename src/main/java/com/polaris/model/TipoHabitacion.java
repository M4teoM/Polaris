package com.polaris.model;

public class TipoHabitacion {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precioPorNoche;
    private String imagenUrl;
    private int metrosCuadrados;
    private int capacidad;
    private String tipoCama;

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
}