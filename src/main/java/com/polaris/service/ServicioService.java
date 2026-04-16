package com.polaris.service;

import com.polaris.errors.ErrorServiceNotFoundException;
import com.polaris.model.Servicio;
import com.polaris.repository.IItemCuentaRepository;
import com.polaris.repository.IPedidoRepository;
import com.polaris.repository.IServicioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private IServicioRepository repository;

    @Autowired
    private IItemCuentaRepository itemCuentaRepository;

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Override
    public List<Servicio> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ErrorServiceNotFoundException(id));
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
        if (!repository.existsById(id)) {
            throw new ErrorServiceNotFoundException(id);
        }

        long itemsCuentaAsociados = itemCuentaRepository.countByServicioId(id);
        long pedidosAsociados = pedidoRepository.countByServicioId(id);

        if (itemsCuentaAsociados > 0 || pedidosAsociados > 0) {
            throw new IllegalStateException(
                "No se puede eliminar el servicio porque tiene datos asociados: " +
                itemsCuentaAsociados + " item(s) de cuenta y " +
                pedidosAsociados + " pedido(s)."
            );
        }

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                "No se puede eliminar el servicio porque tiene datos relacionados en la base de datos.",
                e
            );
        }
    }

    @Override
    @Transactional
    public void eliminarForzado(Long id) {
        if (!repository.existsById(id)) {
            throw new ErrorServiceNotFoundException(id);
        }

        pedidoRepository.deleteByServicioId(id);
        itemCuentaRepository.deleteByServicioId(id);
        repository.deleteById(id);
    }
}