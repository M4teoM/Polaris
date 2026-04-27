package com.polaris.repository;

import com.polaris.model.ItemCuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IItemCuentaRepository extends JpaRepository<ItemCuenta, Long> {
	long countByCuentaId(Long cuentaId);
	long countByServicioId(Long servicioId);
	long deleteByServicioId(Long servicioId);
	List<ItemCuenta> findByCuentaId(Long cuentaId);
}
