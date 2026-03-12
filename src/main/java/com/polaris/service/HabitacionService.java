package com.polaris.service;

import com.polaris.errors.ErrorRoomNotFoundException;
import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitacionService implements IHabitacionService {

    @Autowired
    private IHabitacionRepository repository;

    @Autowired
    private ITipoHabitacionRepository tipoRepo;

    @Override
    public List<Habitacion> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Habitacion obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ErrorRoomNotFoundException(id));
    }

    @Override
    public void crear(Habitacion habitacion) {
        repository.save(habitacion);
    }

    @Override
    public void actualizar(Habitacion habitacion) {
        repository.save(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public void crearConTipo(Habitacion habitacion, Long tipoId) {
        TipoHabitacion tipo = tipoRepo.findById(tipoId)
                .orElseThrow(() -> new ErrorRoomNotFoundException(tipoId));
        habitacion.setTipoHabitacion(tipo);
        repository.save(habitacion);
    }
}