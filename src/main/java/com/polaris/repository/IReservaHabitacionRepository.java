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
}
