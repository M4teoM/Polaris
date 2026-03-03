package com.polaris.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polaris.model.Cliente;
import com.polaris.repository.IClienteRepository;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Override
    public void crear(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public void actualizar(Cliente cliente) {
        clienteRepository.update(cliente);
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}