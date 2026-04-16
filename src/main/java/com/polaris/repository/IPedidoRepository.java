package com.polaris.repository;

import com.polaris.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
	long countByServicioId(Long servicioId);
	long countByClienteId(Long clienteId);
	long deleteByServicioId(Long servicioId);
	long deleteByClienteId(Long clienteId);
	boolean existsByCodigo(String codigo);
}
