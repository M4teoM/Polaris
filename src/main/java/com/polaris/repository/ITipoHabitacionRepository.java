package com.polaris.repository;

import com.polaris.model.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
}