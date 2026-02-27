package com.polaris.repository;

import com.polaris.model.TipoHabitacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TipoHabitacionRepository implements ITipoHabitacionRepository {

    private final Map<Long, TipoHabitacion> baseDeDatos = new HashMap<>();
    private Long nextId = 7L;

    public TipoHabitacionRepository() {
        baseDeDatos.put(1L, new TipoHabitacion(
                1L,
                "Habitación Estándar",
                "Comodidad refinada con vistas parciales y todas las amenidades esenciales para una estadía memorable.",
                250000.0,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800&q=80",
                30, 2, "Cama Queen"
        ));

        baseDeDatos.put(2L, new TipoHabitacion(
                2L,
                "Suite Ejecutiva",
                "Espacio amplio con área de trabajo, vistas a la ciudad y comodidades de primera clase.",
                450000.0,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80",
                45, 2, "Cama King"
        ));

        baseDeDatos.put(3L, new TipoHabitacion(
                3L,
                "Suite VIP",
                "Elegancia exclusiva con servicio personalizado, balcón privado y jacuzzi de lujo.",
                750000.0,
                "https://images.unsplash.com/photo-1591088398332-8a7791972843?w=800&q=80",
                65, 3, "Cama King"
        ));

        baseDeDatos.put(4L, new TipoHabitacion(
                4L,
                "Suite de Lujo",
                "El pináculo de la opulencia. Vista panorámica, baño spa completo y comedor privado.",
                1200000.0,
                "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=800&q=80",
                90, 4, "Cama King"
        ));

        baseDeDatos.put(5L, new TipoHabitacion(
                5L,
                "Habitación Familiar",
                "Diseñada para familias, con dos habitaciones conectadas, área de juegos y todas las comodidades.",
                580000.0,
                "https://images.unsplash.com/photo-1540518614846-7eded433c457?w=800&q=80",
                70, 5, "Dos Camas"
        ));

        baseDeDatos.put(6L, new TipoHabitacion(
                6L,
                "Penthouse",
                "Nuestra joya más exclusiva. Terraza privada con jacuzzi exterior, vista de 360° y mayordomo dedicado.",
                2500000.0,
                "https://images.unsplash.com/photo-1560185007-cde436f6a4d0?w=800&q=80",
                150, 4, "Cama King"
        ));
    }

    @Override
    public List<TipoHabitacion> findAll() {
        return new ArrayList<>(baseDeDatos.values());
    }

    @Override
    public TipoHabitacion findById(Long id) {
        return baseDeDatos.get(id);
    }

    @Override
    public void save(TipoHabitacion tipoHabitacion) {
        tipoHabitacion.setId(nextId++);
        baseDeDatos.put(tipoHabitacion.getId(), tipoHabitacion);
    }

    @Override
    public void update(TipoHabitacion tipoHabitacion) {
        baseDeDatos.put(tipoHabitacion.getId(), tipoHabitacion);
    }

    @Override
    public void deleteById(Long id) {
        baseDeDatos.remove(id);
    }
}