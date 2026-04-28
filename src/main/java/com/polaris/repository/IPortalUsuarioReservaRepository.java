package com.polaris.repository;

import com.polaris.model.ReservaHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPortalUsuarioReservaRepository extends JpaRepository<ReservaHabitacion, Long> {

    /**
     * Obtiene las reservas activas de un cliente usando una regla mixta:
     * 1) la reserva no debe estar cancelada,
     * 2) la fecha de salida debe ser hoy o posterior.
     */
    @Query("SELECT r FROM ReservaHabitacion r " +
            "JOIN FETCH r.cliente c " +
            "JOIN FETCH r.habitacion h " +
            "JOIN FETCH h.tipoHabitacion th " +
            "WHERE c.id = :clienteId " +
            "AND LOWER(r.estado) NOT IN ('cancelado', 'cancelada') " +
            "AND r.fechaCheckOut >= :hoy " +
            "ORDER BY r.fechaCheckIn ASC")
    List<ReservaHabitacion> findReservasActivasByClienteId(@Param("clienteId") Long clienteId,
                                                            @Param("hoy") LocalDate hoy);

    /**
     * Obtiene el historial de reservas de un cliente. Se consideran historial:
     * 1) reservas con fecha de salida anterior a hoy,
     * 2) reservas en estado cancelado/cancelada/finalizada.
     */
    @Query("SELECT r FROM ReservaHabitacion r " +
            "JOIN FETCH r.cliente c " +
            "JOIN FETCH r.habitacion h " +
            "JOIN FETCH h.tipoHabitacion th " +
            "WHERE c.id = :clienteId " +
            "AND (r.fechaCheckOut < :hoy " +
            "OR LOWER(r.estado) IN ('cancelado', 'cancelada', 'finalizada')) " +
            "ORDER BY r.fechaCheckIn DESC")
    List<ReservaHabitacion> findHistorialByClienteId(@Param("clienteId") Long clienteId,
                                                      @Param("hoy") LocalDate hoy);

    /**
     * Busca una reserva por su ID y por el cliente dueño de la reserva.
     * Esto permite validar propiedad en operaciones sensibles como cancelar o actualizar.
     */
    Optional<ReservaHabitacion> findByIdAndClienteId(Long reservaId, Long clienteId);

    /**
     * Verifica si existe otra reserva de la misma habitación que se solape
     * con un rango de fechas. Se excluye la reserva que se está editando.
     */
    @Query("SELECT COUNT(r) > 0 FROM ReservaHabitacion r " +
            "WHERE r.habitacion.id = :habitacionId " +
            "AND r.id <> :reservaId " +
            "AND LOWER(r.estado) NOT IN ('cancelado', 'cancelada') " +
            "AND r.fechaCheckIn < :checkOut " +
            "AND r.fechaCheckOut > :checkIn")
    boolean existsSolapamiento(@Param("habitacionId") Long habitacionId,
                               @Param("reservaId") Long reservaId,
                               @Param("checkIn") LocalDate checkIn,
                               @Param("checkOut") LocalDate checkOut);
}
