package com.polaris.service;

import com.polaris.model.Habitacion;
import java.util.List;

public interface IHabitacionService {
    List<Habitacion> obtenerTodos();
    Habitacion obtenerPorId(Long id);
    void crear(Habitacion habitacion);
    void actualizar(Habitacion habitacion);
    void eliminar(Long id);
}
