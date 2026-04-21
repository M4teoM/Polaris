package com.polaris.service;

import com.polaris.model.Operario;

import java.util.List;
import java.util.Optional;

public interface IOperarioService {

    List<Operario> obtenerTodos();

    Optional<Operario> buscarPorCorreo(String correo);
}
