package com.polaris.repository;

import com.polaris.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    // Busca un usuario por su correo — lo usa Spring Security al hacer login
    Optional<UserEntity> findByCorreo(String correo);

    // Verifica si ya existe un usuario con ese correo — lo usa el DataInitializer
    // para no crear duplicados cada vez que arranca la aplicación
    boolean existsByCorreo(String correo);
}