package com.polaris.repository;

import com.polaris.model.Servicio;

import java.util.List;

public interface IServicioRepository {

    List<Servicio> findAll();

    Servicio findById(Long id);
}