package com.polaris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResumenDTO {
    // Datos de la reserva
    private Long      id;
    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    private String    estado;
    private int       numeroHuespedes;

    // Datos del cliente — solo lo esencial, sin exponer contraseña ni relaciones
    private Long   clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private String clienteCorreo;

    // Datos de la habitación — solo lo esencial, sin traer toda la entidad anidada
    private Long   habitacionId;
    private String habitacionNumero;
    private String habitacionTipo;      // nombre del TipoHabitacion
    private Double precioPorNoche;
}