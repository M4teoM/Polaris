package com.polaris.service;

import com.polaris.dto.ActualizarReservaDTO;
import com.polaris.dto.ClientePerfilDTO;
import com.polaris.dto.ReservaDTO;
import com.polaris.errors.ErrorReservaException;
import com.polaris.errors.ErrorUserNotFoundException;
import com.polaris.model.Cliente;
import com.polaris.model.ReservaHabitacion;
import com.polaris.repository.IPortalUsuarioClienteRepository;
import com.polaris.repository.IPortalUsuarioReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PortalUsuarioService implements IPortalUsuarioService {

    @Autowired
    private IPortalUsuarioClienteRepository clienteRepository;

    @Autowired
    private IPortalUsuarioReservaRepository reservaRepository;

    /**
     * Recupera el perfil del cliente por ID y lo transforma en un DTO plano.
     *
     * Esta operación evita exponer la entidad completa y entrega solamente
     * los datos necesarios para la pantalla "Mi Perfil" del portal.
     */
    @Override
    @Transactional(readOnly = true)
    public ClientePerfilDTO obtenerPerfilCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ErrorUserNotFoundException(clienteId));

        return new ClientePerfilDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getCedula(),
                cliente.getTelefono()
        );
    }

    /**
     * Obtiene las reservas activas del cliente con una regla funcional:
     * reserva no cancelada y con fecha de salida vigente (hoy o futura).
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasActivas(Long clienteId) {
        validarClienteExiste(clienteId);
        return reservaRepository.findReservasActivasByClienteId(clienteId, LocalDate.now())
                .stream()
                .map(this::toReservaDTO)
                .toList();
    }

    /**
     * Obtiene el historial del cliente incluyendo reservas finalizadas
     * por fecha y reservas que fueron canceladas explícitamente.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerHistorialReservas(Long clienteId) {
        validarClienteExiste(clienteId);
        return reservaRepository.findHistorialByClienteId(clienteId, LocalDate.now())
                .stream()
                .map(this::toReservaDTO)
                .toList();
    }

    /**
     * Cambia el estado de una reserva a CANCELADO respetando la propiedad
     * del cliente sobre la reserva.
     *
     * Si la reserva ya está cancelada, retorna la reserva sin volver a guardar,
     * permitiendo idempotencia básica para el frontend.
     */
    @Override
    @Transactional
    public ReservaDTO cancelarReserva(Long clienteId, Long reservaId) {
        ReservaHabitacion reserva = obtenerReservaDelCliente(clienteId, reservaId);

        if ("CANCELADO".equalsIgnoreCase(reserva.getEstado()) ||
                "Cancelada".equalsIgnoreCase(reserva.getEstado())) {
            return toReservaDTO(reserva);
        }

        reserva.setEstado("CANCELADO");
        return toReservaDTO(reservaRepository.save(reserva));
    }

    /**
     * Actualiza las fechas de una reserva y, opcionalmente, el número de huéspedes.
     *
     * Reglas de negocio aplicadas:
     * 1) Ambas fechas son obligatorias para esta operación.
     * 2) checkOut debe ser estrictamente posterior a checkIn.
     * 3) No se puede actualizar una reserva en estado cancelado.
     * 4) No se permite generar solapamiento con otra reserva de la misma habitación.
     */
    @Override
    @Transactional
    public ReservaDTO actualizarReserva(Long clienteId, Long reservaId, ActualizarReservaDTO request) {
        if (request == null || request.getFechaCheckIn() == null || request.getFechaCheckOut() == null) {
            throw new ErrorReservaException("Debes enviar fechaCheckIn y fechaCheckOut para actualizar la reserva.");
        }

        if (!request.getFechaCheckOut().isAfter(request.getFechaCheckIn())) {
            throw new ErrorReservaException("La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        ReservaHabitacion reserva = obtenerReservaDelCliente(clienteId, reservaId);

        if ("CANCELADO".equalsIgnoreCase(reserva.getEstado()) ||
                "Cancelada".equalsIgnoreCase(reserva.getEstado())) {
            throw new ErrorReservaException("No se puede actualizar una reserva cancelada.");
        }

        boolean haySolapamiento = reservaRepository.existsSolapamiento(
                reserva.getHabitacion().getId(),
                reserva.getId(),
                request.getFechaCheckIn(),
                request.getFechaCheckOut()
        );

        if (haySolapamiento) {
            throw new ErrorReservaException("Las nuevas fechas se solapan con otra reserva de la misma habitación.");
        }

        reserva.setFechaCheckIn(request.getFechaCheckIn());
        reserva.setFechaCheckOut(request.getFechaCheckOut());

        if (request.getNumeroHuespedes() != null) {
            if (request.getNumeroHuespedes() <= 0) {
                throw new ErrorReservaException("El número de huéspedes debe ser mayor a cero.");
            }

            int capacidad = reserva.getHabitacion().getTipoHabitacion().getCapacidad();
            if (request.getNumeroHuespedes() > capacidad) {
                throw new ErrorReservaException("El número de huéspedes supera la capacidad de la habitación.");
            }

            reserva.setNumeroHuespedes(request.getNumeroHuespedes());
        }

        return toReservaDTO(reservaRepository.save(reserva));
    }

    /**
     * Valida que el cliente exista antes de ejecutar consultas del portal.
     * Esto evita retornar listas vacías cuando el cliente realmente no existe.
     */
    private void validarClienteExiste(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ErrorUserNotFoundException(clienteId);
        }
    }

    /**
     * Busca una reserva asegurando que pertenezca al cliente autenticado/solicitante.
     * Si no existe esa relación, se retorna un error de negocio controlado.
     */
    private ReservaHabitacion obtenerReservaDelCliente(Long clienteId, Long reservaId) {
        return reservaRepository.findByIdAndClienteId(reservaId, clienteId)
                .orElseThrow(() -> new ErrorReservaException(
                        "La reserva no existe o no pertenece al cliente indicado."));
    }

    /**
     * Convierte la entidad ReservaHabitacion a un DTO plano para evitar objetos anidados.
     */
    private ReservaDTO toReservaDTO(ReservaHabitacion reserva) {
        String nombreCompleto = reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido();

        return new ReservaDTO(
                reserva.getId(),
                reserva.getFechaCheckIn(),
                reserva.getFechaCheckOut(),
                reserva.getEstado(),
                reserva.getNumeroHuespedes(),
                reserva.getCliente().getId(),
                nombreCompleto,
                reserva.getHabitacion().getId(),
                reserva.getHabitacion().getNumero(),
                reserva.getHabitacion().getTipoHabitacion().getId(),
                reserva.getHabitacion().getTipoHabitacion().getNombre()
        );
    }
}
