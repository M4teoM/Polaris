package com.polaris.service;

import com.polaris.model.TipoHabitacion;

import java.util.List;

public interface ITipoHabitacionService {

    List<TipoHabitacion> obtenerTodos();

    TipoHabitacion obtenerPorId(Long id);

    void crear(TipoHabitacion tipoHabitacion);

    void actualizar(TipoHabitacion tipoHabitacion);

    void eliminar(Long id);

    void eliminarForzado(Long id);
}