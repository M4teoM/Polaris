package com.polaris.service;

import com.polaris.model.Servicio;
import com.polaris.repository.IServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService implements IServicioService {

    private final IServicioRepository servicioRepository;

    public ServicioService(IServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id);
    }
}