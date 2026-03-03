package com.polaris.repository;

import java.util.List;
import java.util.Optional;

import com.polaris.model.Cliente;

public interface IClienteRepository {
    List<Cliente> findAll();
    Cliente findById(Long id);
    Optional<Cliente> findByCorreo(String correo);
    void save(Cliente cliente);
    void update(Cliente cliente);
    void deleteById(Long id);
}