package com.polaris.repository;

import com.polaris.model.Servicio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ServicioRepository {

    private List<Servicio> servicios = new ArrayList<>();

    public ServicioRepository() {
        servicios.add(new Servicio(
                1L,
                "Spa Premium",
                "Experiencia completa de relajacion",
                180000.0,
                "https://images.unsplash.com/photo-1544161515-4ab6ce6db874"
        ));

        servicios.add(new Servicio(
                2L,
                "Tour Historico",
                "Recorrido guiado por la ciudad",
                95000.0,
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee"
        ));

        servicios.add(new Servicio(
                3L,
                "Cena Gourmet",
                "Menu degustacion de cinco tiempos",
                220000.0,
                "https://images.unsplash.com/photo-1551218808-94e220e084d2"
        ));
    }

    public List<Servicio> findAll() {
        return servicios;
    }

    public Servicio findById(Long id) {
        return servicios.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}