package com.polaris.service;

import com.polaris.model.Cliente;
import com.polaris.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository repository;

    @Override
    public List<Cliente> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    @Override
    public void crear(Cliente cliente) {
        repository.save(cliente);
    }

    @Override
    public void actualizar(Cliente cliente) {
        repository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}