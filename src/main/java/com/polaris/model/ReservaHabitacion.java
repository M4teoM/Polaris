package com.polaris.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

@Data
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

    // Un cliente puede tener múltiples reservas a lo largo del tiempo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Una habitación puede ser reservada múltiples veces en distintas fechas
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operario")
    private Operario operario;

    @JsonIgnore
    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    private Cuenta cuenta;

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