package com.polaris.repository;

import com.polaris.model.ReservaHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Long> {

    List<ReservaHabitacion> findByClienteId(Long clienteId);

    List<ReservaHabitacion> findByHabitacionId(Long habitacionId);

    long countByClienteId(Long clienteId);

    @Query("SELECT COUNT(r) FROM ReservaHabitacion r " +
            "WHERE r.cliente.id = :clienteId " +
            "AND LOWER(r.estado) <> 'cancelada'")
    long countReservasActivasByClienteId(@Param("clienteId") Long clienteId);

    long countByHabitacionId(Long habitacionId);

    // IDs de habitaciones ocupadas en un rango de fechas (excluyendo canceladas)
    @Query("SELECT r.habitacion.id FROM ReservaHabitacion r " +
            "WHERE r.estado <> 'Cancelada' " +
            "AND r.fechaCheckIn < :checkOut " +
            "AND r.fechaCheckOut > :checkIn")
    List<Long> findHabitacionesOcupadasEnRango(@Param("checkIn") LocalDate checkIn,
                                               @Param("checkOut") LocalDate checkOut);

    // ── NUEVA: carga cliente y habitación en una sola query para el panel admin ──
    @Query("SELECT r FROM ReservaHabitacion r " +
            "JOIN FETCH r.cliente " +
            "JOIN FETCH r.habitacion h " +
            "JOIN FETCH h.tipoHabitacion")
    List<ReservaHabitacion> findAllConDetalle();

    @Query("SELECT r FROM ReservaHabitacion r " +
            "JOIN FETCH r.cliente " +
            "JOIN FETCH r.habitacion h " +
            "JOIN FETCH h.tipoHabitacion " +
            "WHERE LOWER(h.numero) = LOWER(:numeroHabitacion) " +
            "AND LOWER(r.estado) IN ('activa', 'confirmada', 'inactiva')")
    List<ReservaHabitacion> findReservasActivasPorNumeroHabitacion(
            @Param("numeroHabitacion") String numeroHabitacion);

    // Reservas asociadas a clientes cuyo nombre coincide parcialmente con el filtro.
    @Query("SELECT r FROM ReservaHabitacion r JOIN r.cliente c " +
            "WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))")
    List<ReservaHabitacion> findByNombreCliente(@Param("nombreCliente") String nombreCliente);
}
