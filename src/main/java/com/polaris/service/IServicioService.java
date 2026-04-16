package com.polaris.service;

import com.polaris.model.Servicio;
import java.util.List;

public interface IServicioService {
    List<Servicio> obtenerTodos();
    Servicio obtenerPorId(Long id);
    void crear(Servicio servicio);
    void actualizar(Servicio servicio);
    void eliminar(Long id);
    void eliminarForzado(Long id);
}