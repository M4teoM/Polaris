package com.polaris.service;

import com.polaris.model.Servicio;
import com.polaris.repository.IServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private IServicioRepository repository;

    @Override
    public List<Servicio> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void crear(Servicio servicio) {
        repository.save(servicio);
    }

    @Override
    public void actualizar(Servicio servicio) {
        repository.save(servicio);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}