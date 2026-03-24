package com.polaris.repository;

import com.polaris.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByReservaId(Long reservaId);
    List<Cuenta> findByClienteId(Long clienteId);
}
