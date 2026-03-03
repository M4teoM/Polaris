package com.polaris.service;

import com.polaris.model.Habitacion;
import com.polaris.repository.IHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitacionService implements IHabitacionService {

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Override
    public List<Habitacion> obtenerTodos() {
        return habitacionRepository.findAll();
    }

    @Override
    public Habitacion obtenerPorId(Long id) {
        return habitacionRepository.findById(id);
    }

    @Override
    public void crear(Habitacion habitacion) {
        habitacionRepository.save(habitacion);
    }

    @Override
    public void actualizar(Habitacion habitacion) {
        habitacionRepository.update(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        habitacionRepository.deleteById(id);
    }
}
