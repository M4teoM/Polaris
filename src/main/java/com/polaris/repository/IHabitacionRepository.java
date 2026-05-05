package com.polaris.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polaris.model.Habitacion;

@Repository
public interface IHabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByTipoHabitacion_Id(Long tipoId);

    long countByTipoHabitacion_Id(Long tipoId);

    Optional<Habitacion> findByNumeroIgnoreCase(String numero);

    // Habitaciones disponibles que pertenecen a un tipo de habitacion concreto.
    @Query(value = "SELECT * FROM habitacion h WHERE h.estado = 'Disponible' AND h.tipo_habitacion_id = :tipoId", nativeQuery = true)
    List<Habitacion> findDisponiblesByTipoId(@Param("tipoId") Long tipoId);

    // Total de habitaciones registradas en un piso determinado.
    @Query(value = "SELECT COUNT(*) FROM habitacion h WHERE h.piso = :piso", nativeQuery = true)
    long countByPiso(@Param("piso") int piso);
}
