package com.polaris.service;

import com.polaris.errors.ErrorUserNotFoundException;
import com.polaris.model.Cliente;
import com.polaris.model.Cuenta;
import com.polaris.model.ReservaHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.ICuentaRepository;
import com.polaris.repository.IPedidoRepository;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Autowired
    private IPedidoRepository pedidoRepo;

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

        long pedidosAsociados = pedidoRepo.countByClienteId(id);
        if (pedidosAsociados > 0) {
            throw new IllegalStateException(
                "El cliente \"" + cliente.getNombre() + " " + cliente.getApellido() +
                "\" no se puede eliminar porque tiene " + pedidosAsociados + " pedido(s) asociado(s)."
            );
        }

        List<Cuenta> cuentasCliente = cuentaRepo.findByClienteId(id);
        if (!cuentasCliente.isEmpty()) {
            cuentaRepo.deleteAll(cuentasCliente);
        }

        List<ReservaHabitacion> reservasCliente = reservaRepo.findByClienteId(id);
        if (!reservasCliente.isEmpty()) {
            reservaRepo.deleteAll(reservasCliente);
        }

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                "No se pudo eliminar el cliente porque tiene datos relacionados en la base de datos.",
                e
            );
        }
    }

    @Override
    @Transactional
    public void eliminarForzado(Long id) {
        obtenerPorId(id);

        pedidoRepo.deleteByClienteId(id);

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