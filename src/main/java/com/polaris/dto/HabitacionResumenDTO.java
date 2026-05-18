package com.polaris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionResumenDTO {
    // Datos de la habitación física
    private Long   id;
    private String numero;
    private int    piso;
    private String estado;

    // Datos del TipoHabitacion aplanados — evita el objeto anidado en el JSON
    private Long   tipoId;
    private String tipoNombre;
    private Double precioPorNoche;
    private String imagenUrl;
    private int    capacidadPersonas;
    private String tipoCama;
}