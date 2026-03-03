package com.polaris.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.polaris.model.Cliente;

@Repository
public class ClienteRepository implements IClienteRepository {

    private final Map<Long, Cliente> baseDeDatos = new HashMap<>();
    private Long nextId = 3L;

    public ClienteRepository() {
        baseDeDatos.put(1L, new Cliente(
            1L,
            "Samuel",
            "Tovar",
            "samutovar10@gmail.com", "8888"));
            
        baseDeDatos.put(2L, new Cliente(
            2L,
            "Mateo",
            "Madrigal",
            "pruebas@a.com",
            "aaaa"));
    }

    @Override
    public List<Cliente> findAll() {
        return new ArrayList<>(baseDeDatos.values());
    }

    @Override
    public Cliente findById(Long id) {
        return baseDeDatos.get(id);
    }

    @Override
    public Optional<Cliente> findByCorreo(String correo) {
        return baseDeDatos.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    @Override
    public void save(Cliente cliente) {
        cliente.setId(nextId++);
        baseDeDatos.put(cliente.getId(), cliente);
    }

    @Override
    public void update(Cliente cliente) {
        baseDeDatos.put(cliente.getId(), cliente);
    }

    @Override
    public void deleteById(Long id) {
        baseDeDatos.remove(id);
    }
}