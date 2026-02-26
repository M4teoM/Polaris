package com.polaris.service;

import com.polaris.model.Servicio;
import com.polaris.repository.IServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private IServicioRepository servicioRepository;

    @Override
    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id);
    }
}