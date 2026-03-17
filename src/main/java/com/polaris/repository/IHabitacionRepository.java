package com.polaris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polaris.model.Habitacion;

@Repository
public interface IHabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByTipoHabitacion_Id(Long tipoId);

    long countByTipoHabitacion_Id(Long tipoId);
}