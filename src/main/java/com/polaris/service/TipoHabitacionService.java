package com.polaris.service;

import com.polaris.errors.ErrorRoomNotFoundException;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoHabitacionService implements ITipoHabitacionService {

    @Autowired
    private ITipoHabitacionRepository repository;

    @Override
    public List<TipoHabitacion> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public TipoHabitacion obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ErrorRoomNotFoundException(id));
    }

    @Override
    public void crear(TipoHabitacion tipo) {
        repository.save(tipo);
    }

    @Override
    public void actualizar(TipoHabitacion tipo) {
        repository.save(tipo);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}