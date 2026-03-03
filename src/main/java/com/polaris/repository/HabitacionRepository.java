package com.polaris.repository;

import com.polaris.model.Habitacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HabitacionRepository implements IHabitacionRepository {

    private final Map<Long, Habitacion> baseDeDatos = new HashMap<>();
    private Long nextId = 7L;

    public HabitacionRepository() {
        // Piso 1 - Habitaciones Estándar (tipoId=1)
        baseDeDatos.put(1L, new Habitacion(1L, "101", 1, "Disponible", 1L));
        baseDeDatos.put(2L, new Habitacion(2L, "102", 1, "Ocupada",    1L));
        baseDeDatos.put(3L, new Habitacion(3L, "103", 1, "Disponible", 1L));
        // Piso 2 - Suites Ejecutivas (tipoId=2)
        baseDeDatos.put(4L, new Habitacion(4L, "201", 2, "Disponible", 2L));
        baseDeDatos.put(5L, new Habitacion(5L, "202", 2, "Mantenimiento", 2L));
        // Piso 3 - Suite VIP (tipoId=3)
        baseDeDatos.put(6L, new Habitacion(6L, "301", 3, "Disponible", 3L));
    }

    @Override
    public List<Habitacion> findAll() {
        return new ArrayList<>(baseDeDatos.values());
    }

    @Override
    public Habitacion findById(Long id) {
        return baseDeDatos.get(id);
    }

    @Override
    public void save(Habitacion habitacion) {
        habitacion.setId(nextId++);
        baseDeDatos.put(habitacion.getId(), habitacion);
    }

    @Override
    public void update(Habitacion habitacion) {
        baseDeDatos.put(habitacion.getId(), habitacion);
    }

    @Override
    public void deleteById(Long id) {
        baseDeDatos.remove(id);
    }
}
