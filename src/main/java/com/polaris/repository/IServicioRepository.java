package com.polaris.repository;

import com.polaris.model.Servicio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Long> {

	// Servicios cuyo precio cae dentro de un rango definido.
	@Query("SELECT s FROM Servicio s WHERE s.precio BETWEEN :minPrecio AND :maxPrecio")
	List<Servicio> findByPrecioBetweenCustom(@Param("minPrecio") Double minPrecio,
											 @Param("maxPrecio") Double maxPrecio);

	// Servicios cuya categoria contiene la palabra clave indicada.
	@Query("SELECT s FROM Servicio s WHERE LOWER(s.categoria) LIKE LOWER(CONCAT('%', :palabraClave, '%'))")
	List<Servicio> findByCategoriaContainingIgnoreCaseCustom(@Param("palabraClave") String palabraClave);
}