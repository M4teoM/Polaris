package com.polaris.repository;

import com.polaris.model.Habitacion;
import java.util.List;

public interface IHabitacionRepository {
    List<Habitacion> findAll();
    Habitacion findById(Long id);
    void save(Habitacion habitacion);
    void update(Habitacion habitacion);
    void deleteById(Long id);
}
