package com.polaris.service;

import com.polaris.errors.ErrorUserNotFoundException;
import com.polaris.model.Cliente;
import com.polaris.model.Cuenta;
import com.polaris.model.ReservaHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.ICuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import com.polaris.repository.IReservaHabitacionRepository;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository repository;

    @Autowired
    private IReservaHabitacionRepository reservaRepo;

    @Autowired
    private ICuentaRepository cuentaRepo;

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
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        long reservas = reservaRepo.countReservasActivasByClienteId(id);
        if (reservas > 0) {
            throw new IllegalStateException(
                "El cliente \"" + cliente.getNombre() + " " + cliente.getApellido() +
                "\" no se puede eliminar porque cuenta con " + reservas + " reserva(s) activas.");
        }

        List<Cuenta> cuentasCliente = cuentaRepo.findByClienteId(id);
        if (!cuentasCliente.isEmpty()) {
            cuentaRepo.deleteAll(cuentasCliente);
        }

        List<ReservaHabitacion> reservasCliente = reservaRepo.findByClienteId(id);
        if (!reservasCliente.isEmpty()) {
            reservaRepo.deleteAll(reservasCliente);
        }

        repository.deleteById(id);
    }
}