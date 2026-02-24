package com.polaris.service;

import com.polaris.model.Servicio;
import com.polaris.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    // Retorna Servicio directamente igual que el Repository (sin Optional)
    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id);
    }
}