package com.polaris.service;

import com.polaris.errors.ErrorRoomNotFoundException;
import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.polaris.repository.IReservaHabitacionRepository;

@Service
public class HabitacionService implements IHabitacionService {

    @Autowired
    private IHabitacionRepository repository;

    @Autowired
    private ITipoHabitacionRepository tipoRepo;

    @Autowired
    private IReservaHabitacionRepository reservaRepo;

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
        long reservas = reservaRepo.countByHabitacionId(id);
        if (reservas > 0) {
            throw new IllegalStateException(
                "No se puede eliminar la habitación porque tiene " + reservas + " reserva(s) asociada(s). " +
                "Elimine primero las reservas asociadas.");
        }
        repository.deleteById(id);
    }

    public void crearConTipo(Habitacion habitacion, Long tipoId) {
        TipoHabitacion tipo = tipoRepo.findById(tipoId)
                .orElseThrow(() -> new ErrorRoomNotFoundException(tipoId));
        habitacion.setTipoHabitacion(tipo);
        repository.save(habitacion);
    }
}