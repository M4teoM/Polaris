package com.polaris.repository;

import com.polaris.model.ItemCuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemCuentaRepository extends JpaRepository<ItemCuenta, Long> {
	long countByCuentaId(Long cuentaId);
	long countByServicioId(Long servicioId);
	long deleteByServicioId(Long servicioId);
}
