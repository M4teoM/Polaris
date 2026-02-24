package com.polaris.repository;

import com.polaris.model.Servicio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServicioRepository implements IServicioRepository {

    private final Map<Long, Servicio> baseDeDatos = new HashMap<>();

    public ServicioRepository() {
        baseDeDatos.put(1L, new Servicio(
                1L,
                "Spa Premium",
                "Experiencia completa de relajacion",
                "Nuestro Spa Premium ofrece una experiencia de relajación total. Incluye masajes terapéuticos, " +
                "tratamientos faciales con productos de alta gama, aromaterapia, sauna finlandesa y jacuzzi privado. " +
                "Nuestros terapeutas certificados le brindarán atención personalizada durante 2 horas completas " +
                "de puro bienestar en un ambiente de tranquilidad absoluta.",
                180000.0,
                "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800&q=80",
                "Bienestar"
        ));

        baseDeDatos.put(2L, new Servicio(
                2L,
                "Tour Historico",
                "Recorrido guiado por la ciudad",
                "Un recorrido exclusivo por los lugares más emblemáticos e históricos de la ciudad. " +
                "Nuestro guía bilingüe certificado le llevará a monumentos, museos y barrios históricos " +
                "con anécdotas únicas. Incluye transporte privado, refrigerios gourmet y acceso a zonas " +
                "de difícil acceso para el público general. Duración aproximada: 4 horas.",
                95000.0,
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800&q=80",
                "Turismo"
        ));

        baseDeDatos.put(3L, new Servicio(
                3L,
                "Cena Gourmet",
                "Menu degustacion de cinco tiempos",
                "Una experiencia gastronómica de alto nivel diseñada por nuestro chef ejecutivo con formación " +
                "internacional. El menú de degustación de 5 tiempos fusiona lo mejor de la cocina local con " +
                "técnicas de vanguardia. Incluye maridaje de vinos seleccionados, mesa privada con vista " +
                "panorámica y servicio de sommelier personalizado.",
                220000.0,
                "https://images.unsplash.com/photo-1551218808-94e220e084d2?w=800&q=80",
                "Gastronomía"
        ));

        baseDeDatos.put(4L, new Servicio(
                4L,
                "test test",
                "awawawawa",
                "Una locura chaval",
                199990.0,
                "https://static0.cbrimages.com/wordpress/wp-content/uploads/2023/08/reze-chainsaw-man.jpg?w=1200&h=900&fit=crop",
                "final feliz"
        ));
    }

    @Override
    public List<Servicio> findAll() {
        return new ArrayList<>(baseDeDatos.values());
    }

    @Override
    public Servicio findById(Long id) {
        return baseDeDatos.get(id);
    }
}