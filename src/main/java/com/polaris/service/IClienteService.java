package com.polaris.service;

import java.util.List;
import java.util.Optional;

import com.polaris.model.Cliente;

public interface IClienteService {
    List<Cliente> obtenerTodos();
    Cliente obtenerPorId(Long id);
    Optional<Cliente> buscarPorCorreo(String correo);
    void crear(Cliente cliente);
    void actualizar(Cliente cliente);
    void eliminar(Long id);
    void eliminarForzado(Long id);
}