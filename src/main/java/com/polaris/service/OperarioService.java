package com.polaris.service;

import com.polaris.model.Operario;
import com.polaris.repository.IOperarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperarioService implements IOperarioService {

    @Autowired
    private IOperarioRepository repository;

    @Override
    public List<Operario> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Operario> buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo);
    }
}
