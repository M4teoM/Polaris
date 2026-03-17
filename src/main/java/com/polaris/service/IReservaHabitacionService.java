package com.polaris.service;

import com.polaris.model.ReservaHabitacion;

import java.util.List;

public interface IReservaHabitacionService {

    List<ReservaHabitacion> obtenerTodos();

    ReservaHabitacion obtenerPorId(Long id);

    void crear(ReservaHabitacion reserva);

    void actualizar(ReservaHabitacion reserva);

    void eliminar(Long id);

    List<ReservaHabitacion> obtenerPorCliente(Long clienteId);

    List<ReservaHabitacion> obtenerPorHabitacion(Long habitacionId);
}
