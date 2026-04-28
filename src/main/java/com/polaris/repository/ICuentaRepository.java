package com.polaris.repository;

import com.polaris.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByReservaId(Long reservaId);

    List<Cuenta> findByClienteId(Long clienteId);

    @Query("SELECT c FROM Cuenta c " +
            "JOIN FETCH c.reserva r " +
            "JOIN FETCH r.cliente " +
            "JOIN FETCH r.habitacion h " +
            "JOIN FETCH h.tipoHabitacion " +
            "LEFT JOIN FETCH c.items i " +
            "LEFT JOIN FETCH i.servicio")
    List<Cuenta> findAllConDetalle();
}
