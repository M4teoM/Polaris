package com.polaris.service;

import com.polaris.errors.ErrorReservaException;
import com.polaris.errors.ErrorRoomNotFoundException;
import com.polaris.errors.ErrorUserNotFoundException;
import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaHabitacionService implements IReservaHabitacionService {

    @Autowired
    private IReservaHabitacionRepository repository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IHabitacionRepository habitacionRepository;

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

    @Override
    public void actualizar(ReservaHabitacion reserva) {
        ReservaHabitacion existente = obtenerPorId(reserva.getId());
        if (reserva.getCliente() == null)    reserva.setCliente(existente.getCliente());
        if (reserva.getHabitacion() == null) reserva.setHabitacion(existente.getHabitacion());
        repository.save(reserva);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id))
            throw new RuntimeException("Reserva con ID " + id + " no encontrada.");
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

    @Override
    public void crearDesdeDetalle(Long clienteId, Long tipoHabitacionId,
                                  LocalDate checkIn, LocalDate checkOut, int numeroHuespedes) {

        // Validar fechas
        if (!checkOut.isAfter(checkIn))
            throw new ErrorReservaException("La fecha de salida debe ser posterior a la de entrada.");
        if (checkIn.isBefore(LocalDate.now()))
            throw new ErrorReservaException("La fecha de entrada no puede ser en el pasado.");

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ErrorUserNotFoundException(clienteId));

        // Buscar habitaciones ocupadas en ese rango
        List<Long> ocupadas = repository.findHabitacionesOcupadasEnRango(checkIn, checkOut);

        // De las habitaciones del tipo solicitado, encontrar una que no esté ocupada
        Habitacion disponible = habitacionRepository
                .findByTipoHabitacion_Id(tipoHabitacionId)
                .stream()
                .filter(h -> !ocupadas.contains(h.getId()))
                .findFirst()
                .orElseThrow(() -> new ErrorReservaException(
                        "No hay habitaciones disponibles de este tipo para las fechas seleccionadas."));

        // Validar capacidad contra el tipo de la habitación encontrada
        if (numeroHuespedes > disponible.getTipoHabitacion().getCapacidad())
            throw new ErrorReservaException(
                    "El número de huéspedes supera la capacidad máxima (" +
                            disponible.getTipoHabitacion().getCapacidad() + " personas).");

        repository.save(new ReservaHabitacion(
                checkIn, checkOut, "Confirmada", numeroHuespedes, cliente, disponible));
    }

    @Override
    public void cancelar(Long reservaId, Long clienteId) {
        ReservaHabitacion reserva = obtenerPorId(reservaId);

        if (!reserva.getCliente().getId().equals(clienteId))
            throw new ErrorReservaException("No tienes permiso para cancelar esta reserva.");

        reserva.setEstado("Cancelada");
        repository.save(reserva);
    }
}
