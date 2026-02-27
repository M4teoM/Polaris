package com.polaris.service;

import com.polaris.model.TipoHabitacion;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoHabitacionService implements ITipoHabitacionService {

    @Autowired
    private ITipoHabitacionRepository tipoHabitacionRepository;

    @Override
    public List<TipoHabitacion> obtenerTodos() {
        return tipoHabitacionRepository.findAll();
    }

    @Override
    public TipoHabitacion obtenerPorId(Long id) {
        return tipoHabitacionRepository.findById(id);
    }

    @Override
    public void crear(TipoHabitacion tipoHabitacion) {
        tipoHabitacionRepository.save(tipoHabitacion);
    }

    @Override
    public void actualizar(TipoHabitacion tipoHabitacion) {
        tipoHabitacionRepository.update(tipoHabitacion);
    }

    @Override
    public void eliminar(Long id) {
        tipoHabitacionRepository.deleteById(id);
    }
}