package com.polaris.service;

import com.polaris.model.ReservaHabitacion;

import java.time.LocalDate;
import java.util.List;

public interface IReservaHabitacionService {

    List<ReservaHabitacion> obtenerTodos();

    ReservaHabitacion obtenerPorId(Long id);

    void crear(ReservaHabitacion reserva);

    void actualizar(ReservaHabitacion reserva);

    void eliminar(Long id);

    List<ReservaHabitacion> obtenerPorCliente(Long clienteId);

    List<ReservaHabitacion> obtenerPorHabitacion(Long habitacionId);

    // Crea una reserva buscando automáticamente una habitación disponible del tipo solicitado
    void crearDesdeDetalle(Long clienteId, Long tipoHabitacionId,
                           LocalDate checkIn, LocalDate checkOut, int numeroHuespedes);

    void cancelar(Long reservaId, Long clienteId);
}
