package com.polaris.repository;

import com.polaris.model.Operario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOperarioRepository extends JpaRepository<Operario, Long> {
    Optional<Operario> findByCorreo(String correo);
}
