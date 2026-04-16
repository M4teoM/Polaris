package com.polaris.service;

import com.polaris.errors.ErrorRoomNotFoundException;
import com.polaris.model.Cuenta;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.ICuentaRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TipoHabitacionService implements ITipoHabitacionService {

    @Autowired
    private ITipoHabitacionRepository repository;

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private IReservaHabitacionRepository reservaRepository;

    @Autowired
    private ICuentaRepository cuentaRepository;

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
        long habitacionesAsociadas = habitacionRepository.countByTipoHabitacion_Id(id);
        if (habitacionesAsociadas > 0) {
            throw new IllegalStateException("No se puede eliminar este tipo porque esta asociado a "
                    + habitacionesAsociadas + " habitacion(es).");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarForzado(Long id) {
        TipoHabitacion tipo = obtenerPorId(id);

        List<Habitacion> habitaciones = habitacionRepository.findByTipoHabitacion_Id(tipo.getId());

        for (Habitacion habitacion : habitaciones) {
            List<ReservaHabitacion> reservas = reservaRepository.findByHabitacionId(habitacion.getId());

            for (ReservaHabitacion reserva : reservas) {
                Optional<Cuenta> cuenta = cuentaRepository.findByReservaId(reserva.getId());
                cuenta.ifPresent(cuentaRepository::delete);
            }

            if (!reservas.isEmpty()) {
                reservaRepository.deleteAll(reservas);
            }
        }

        if (!habitaciones.isEmpty()) {
            habitacionRepository.deleteAll(habitaciones);
        }

        repository.deleteById(tipo.getId());
    }
}