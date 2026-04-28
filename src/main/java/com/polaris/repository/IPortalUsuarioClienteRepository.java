package com.polaris.repository;

import com.polaris.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPortalUsuarioClienteRepository extends JpaRepository<Cliente, Long> {
}
