package com.polaris.service;

import com.polaris.errors.ErrorUserNotFoundException;
import com.polaris.model.Cliente;
import com.polaris.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.polaris.repository.IReservaHabitacionRepository;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository repository;

    @Autowired
    private IReservaHabitacionRepository reservaRepo;

    @Override
    public List<Cliente> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ErrorUserNotFoundException(id));
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    @Override
    public void crear(Cliente cliente) {
        if (repository.findByCorreo(cliente.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Este correo ya está registrado.");
        }
        repository.save(cliente);
    }

    @Override
    public void actualizar(Cliente cliente) {
        repository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        long reservas = reservaRepo.countByClienteId(id);
        if (reservas > 0) {
            throw new IllegalStateException(
                "No se puede eliminar el cliente porque tiene " + reservas + " reserva(s) activa(s). " +
                "Elimine primero las reservas asociadas.");
        }
        repository.deleteById(id);
    }
}