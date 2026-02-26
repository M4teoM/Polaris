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
                "Aventura en Helicoptero",
                "Vuelo panoramico sobre la ciudad",
                "Disfrute de una experiencia exclusiva sobrevolando los principales paisajes urbanos y naturales de la region. Incluye piloto certificado, audio guia personalizada y brindis premium al aterrizar. Duracion aproximada: 45 minutos.",
                450000.0,
                "https://images.unsplash.com/photo-1508615070457-7baeba4003ab?w=800&q=80",
                "Aventura"
        ));

        baseDeDatos.put(5L, new Servicio(
                5L,
                "Sesion Fotografica Profesional",
                "Captura momentos inolvidables",
                "Sesion fotografica profesional en locaciones iconicas del hotel y sus alrededores. Incluye fotografo experto, edicion digital avanzada y entrega de 25 fotografias en alta resolucion. Ideal para parejas o celebraciones especiales.",
                320000.0,
                "https://images.unsplash.com/photo-1519741497674-611481863552?w=800&q=80",
                "Experiencias"
        ));

        baseDeDatos.put(6L, new Servicio(
                6L,
                "Clase Privada de Cocina",
                "Aprende con nuestro chef ejecutivo",
                "Clase exclusiva donde aprendera tecnicas gourmet junto a nuestro chef ejecutivo. Incluye ingredientes premium, preparacion de tres platos y degustacion final con maridaje seleccionado. Duracion aproximada: 3 horas.",
                210000.0,
                "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=800&q=80",
                "Gastronomia"
        ));

        baseDeDatos.put(7L, new Servicio(
                7L,
                "Noche Romantica Deluxe",
                "Decoracion especial y amenidades premium",
                "Transforme su estancia con una ambientacion romantica que incluye decoracion floral, velas aromaticas, botella de champagne, fresas con chocolate y desayuno especial en la habitacion al dia siguiente.",
                280000.0,
                "https://images.unsplash.com/photo-1511988617509-a57c8a288659?w=800&q=80",
                "Romantico"
        ));

        baseDeDatos.put(8L, new Servicio(
                8L,
                "Ruta en Yate Privado",
                "Experiencia nautica exclusiva",
                "Navegue en un yate privado con tripulacion certificada y servicio premium a bordo. Incluye bebidas, snacks gourmet y recorrido por paisajes costeros seleccionados. Ideal para celebraciones o experiencias VIP. Duracion aproximada: 3 horas.",
                520000.0,
                "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?w=800&q=80",
                "Lujo"
        ));

        baseDeDatos.put(9L, new Servicio(
                9L,
                "Sesion de Yoga al Amanecer",
                "Bienestar con vista panoramica",
                "Clase privada de yoga guiada por instructor certificado en terraza panoramica. Incluye kit de bienestar, hidratacion natural y desayuno saludable posterior a la sesion. Duracion aproximada: 90 minutos.",
                120000.0,
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&q=80",
                "Bienestar"
        ));

        baseDeDatos.put(10L, new Servicio(
                10L,
                "Experiencia Sommelier Privada",
                "Cata exclusiva de vinos internacionales",
                "Degustacion privada dirigida por sommelier profesional con seleccion de vinos internacionales premium. Incluye maridaje con tabla de quesos artesanales y explicacion tecnica de cada etiqueta. Duracion aproximada: 2 horas.",
                260000.0,
                "https://images.unsplash.com/photo-1510626176961-4b57d4fbad03?w=800&q=80",
                "Gastronomia"
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