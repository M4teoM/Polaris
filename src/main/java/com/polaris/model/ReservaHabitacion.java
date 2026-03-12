package com.polaris.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva_habitacion")
public class ReservaHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaCheckIn;

    @Column(nullable = false)
    private LocalDate fechaCheckOut;

    @Column(length = 20, nullable = false)
    private String estado;

    @Column(nullable = false)
    private int numeroHuespedes;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @OneToOne
    @JoinColumn(name = "habitacion_id", nullable = false, unique = true)
    private Habitacion habitacion;

    public ReservaHabitacion(LocalDate fechaCheckIn, LocalDate fechaCheckOut,
                             String estado, int numeroHuespedes,
                             Cliente cliente, Habitacion habitacion) {
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.estado = estado;
        this.numeroHuespedes = numeroHuespedes;
        this.cliente = cliente;
        this.habitacion = habitacion;
    }

   
}