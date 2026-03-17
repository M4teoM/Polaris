package com.polaris.service;

import com.polaris.model.ReservaHabitacion;
import com.polaris.repository.IReservaHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaHabitacionService implements IReservaHabitacionService {

    @Autowired
    private IReservaHabitacionRepository repository;

    @Override
    public List<ReservaHabitacion> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public ReservaHabitacion obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva con ID " + id + " no encontrada."));
    }

    @Override
    public void crear(ReservaHabitacion reserva) {
        repository.save(reserva);
    }

    // Al actualizar se conservan las relaciones con cliente y habitación intactas
    @Override
    public void actualizar(ReservaHabitacion reserva) {
        ReservaHabitacion existente = obtenerPorId(reserva.getId());
        // Preservar relaciones si no se envían en el update
        if (reserva.getCliente() == null) {
            reserva.setCliente(existente.getCliente());
        }
        if (reserva.getHabitacion() == null) {
            reserva.setHabitacion(existente.getHabitacion());
        }
        repository.save(reserva);
    }

    // Eliminar directamente: la reserva no tiene dependencias hacia otras entidades
    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Reserva con ID " + id + " no encontrada.");
        }
        repository.deleteById(id);
    }

    @Override
    public List<ReservaHabitacion> obtenerPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Override
    public List<ReservaHabitacion> obtenerPorHabitacion(Long habitacionId) {
        return repository.findByHabitacionId(habitacionId);
    }
}
