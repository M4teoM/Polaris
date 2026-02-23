package com.polaris.service;

import com.polaris.model.Servicio;
import com.polaris.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository repository;

    public ServicioService(ServicioRepository repository) {
        this.repository = repository;
    }

    public List<Servicio> obtenerTodos() {
        return repository.findAll();
    }

    public Servicio obtenerPorId(Long id) {
        return repository.findById(id);
    }
}