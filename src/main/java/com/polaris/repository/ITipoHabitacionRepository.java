package com.polaris.repository;

import com.polaris.model.TipoHabitacion;

import java.util.List;

public interface ITipoHabitacionRepository {

    List<TipoHabitacion> findAll();

    TipoHabitacion findById(Long id);

    void save(TipoHabitacion tipoHabitacion);

    void update(TipoHabitacion tipoHabitacion);

    void deleteById(Long id);
}