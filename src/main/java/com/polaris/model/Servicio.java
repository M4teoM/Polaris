package com.polaris.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(length = 1500)
    private String descripcionDetallada;

    @Column(nullable = false)
    private Double precio;

    @Column(length = 300)
    private String imagenUrl;

    @Column(length = 50, nullable = false)
    private String categoria;

    @Column(length = 50)
    private String duracion;

    @Column(length = 200)
    private String horario;

    @Column(length = 1000)
    private String incluye;

    @Column(length = 1000)
    private String destacados;

    @Transient
    public String[] getIncluyeLista() {
        if (incluye == null || incluye.isBlank()) return new String[0];
        return incluye.split("\\|");
    }

    @Transient
    public String[] getDestacadosLista() {
        if (destacados == null || destacados.isBlank()) return new String[0];
        return destacados.split("\\|");
    }
}