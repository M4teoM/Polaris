package com.polaris.config;

import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.Servicio;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IServicioRepository;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private ITipoHabitacionRepository tipoRepo;
    @Autowired private IHabitacionRepository habitacionRepo;
    @Autowired private IClienteRepository clienteRepo;
    @Autowired private IServicioRepository servicioRepo;

    @Override
    public void run(String... args) {

        // Solo insertar si la BD está vacía
        if (tipoRepo.count() > 0) return;

        // ── 5 Tipos de Habitación ─────────────────────────────────────────
        TipoHabitacion estandar = tipoRepo.save(new TipoHabitacion(
            "Habitación Estándar",
            "Comodidad refinada con vistas parciales y todas las amenidades esenciales.",
            250000.0, "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800&q=80",
            30, 2, "Cama Queen"));

        TipoHabitacion ejecutiva = tipoRepo.save(new TipoHabitacion(
            "Suite Ejecutiva",
            "Espacio amplio con área de trabajo, vistas a la ciudad y comodidades de primera clase.",
            450000.0, "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80",
            45, 2, "Cama King"));

        TipoHabitacion vip = tipoRepo.save(new TipoHabitacion(
            "Suite VIP",
            "Elegancia exclusiva con servicio personalizado, balcón privado y jacuzzi de lujo.",
            750000.0, "https://images.unsplash.com/photo-1591088398332-8a7791972843?w=800&q=80",
            65, 3, "Cama King"));

        TipoHabitacion familiar = tipoRepo.save(new TipoHabitacion(
            "Habitación Familiar",
            "Diseñada para familias, con dos habitaciones conectadas y área de juegos.",
            580000.0, "https://images.unsplash.com/photo-1540518614846-7eded433c457?w=800&q=80",
            70, 5, "Dos Camas"));

        TipoHabitacion penthouse = tipoRepo.save(new TipoHabitacion(
            "Penthouse",
            "Nuestra joya más exclusiva. Terraza privada con jacuzzi exterior y vista de 360°.",
            2500000.0, "https://images.unsplash.com/photo-1560185007-cde436f6a4d0?w=800&q=80",
            150, 4, "Cama King"));

        // ── 50 Habitaciones ───────────────────────────────────────────────
        String[] estados = {"Disponible", "Ocupada", "Mantenimiento"};

        // Piso 1: 101-110 (Estándar)
        for (int i = 1; i <= 10; i++)
            habitacionRepo.save(new Habitacion("10" + i, 1, estados[i % 3], estandar));

        // Piso 2: 201-205 (Estándar) 206-210 (Ejecutiva)
        for (int i = 1; i <= 5; i++)
            habitacionRepo.save(new Habitacion("20" + i, 2, estados[i % 3], estandar));
        for (int i = 6; i <= 10; i++)
            habitacionRepo.save(new Habitacion("20" + i, 2, estados[i % 3], ejecutiva));

        // Piso 3: 301-305 (Ejecutiva) 306-310 (VIP)
        for (int i = 1; i <= 5; i++)
            habitacionRepo.save(new Habitacion("30" + i, 3, estados[i % 3], ejecutiva));
        for (int i = 6; i <= 10; i++)
            habitacionRepo.save(new Habitacion("30" + i, 3, estados[i % 3], vip));

        // Piso 4: 401-405 (VIP) 406-410 (Familiar)
        for (int i = 1; i <= 5; i++)
            habitacionRepo.save(new Habitacion("40" + i, 4, estados[i % 3], vip));
        for (int i = 6; i <= 10; i++)
            habitacionRepo.save(new Habitacion("40" + i, 4, estados[i % 3], familiar));

        // Piso 5: 501-508 (Familiar) 509-510 (Penthouse)
        for (int i = 1; i <= 8; i++)
            habitacionRepo.save(new Habitacion("50" + i, 5, estados[i % 3], familiar));
        habitacionRepo.save(new Habitacion("509", 5, "Disponible", penthouse));
        habitacionRepo.save(new Habitacion("510", 5, "Disponible", penthouse));

        // ── 10 Clientes ───────────────────────────────────────────────────
        clienteRepo.save(new Cliente("Samuel",    "Tovar",      "samutovar10@gmail.com",      "8888"));
        clienteRepo.save(new Cliente("Mateo",     "Madrigal",   "mateo.madrigal@email.com",   "pass123"));
        clienteRepo.save(new Cliente("Valentina", "Rodríguez",  "vale.rod@email.com",          "val456"));
        clienteRepo.save(new Cliente("Andrés",    "García",     "andres.garcia@email.com",     "and789"));
        clienteRepo.save(new Cliente("Isabella",  "Martínez",   "isa.martinez@email.com",      "isa321"));
        clienteRepo.save(new Cliente("Sebastián", "López",      "seba.lopez@email.com",        "seb654"));
        clienteRepo.save(new Cliente("Camila",    "Hernández",  "cami.hndz@email.com",         "cam987"));
        clienteRepo.save(new Cliente("Daniel",    "Torres",     "daniel.torres@email.com",     "dan111"));
        clienteRepo.save(new Cliente("Sofia",     "Ramírez",    "sofia.ramirez@email.com",     "sof222"));
        clienteRepo.save(new Cliente("Juliana",   "Vargas",     "juli.vargas@email.com",       "jul333"));

        // ── Servicios ─────────────────────────────────────────────────────
        servicioRepo.save(new Servicio(
            "Restaurante Estelar",
            "Gastronomía de autor con ingredientes locales e internacionales.",
            "Nuestro chef ejecutivo ofrece una experiencia culinaria única con menús de temporada.",
            85000.0, "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&q=80",
            "Gastronomía"));

        servicioRepo.save(new Servicio(
            "Spa Polaris",
            "Tratamientos de bienestar y relajación de clase mundial.",
            "Disfruta de masajes, aromaterapia, sauna y más en nuestro spa de lujo de 500m².",
            120000.0, "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800&q=80",
            "Bienestar"));

        servicioRepo.save(new Servicio(
            "Piscina Infinity",
            "Piscina al borde infinito con vista panorámica de la ciudad.",
            "Nuestra piscina de 25 metros en el piso 10 ofrece una experiencia única con bar acuático.",
            35000.0, "https://images.unsplash.com/photo-1540541338287-41700207dee6?w=800&q=80",
            "Recreación"));

        System.out.println("✅ Datos insertados: 5 tipos, 50 habitaciones, 10 clientes, 3 servicios");
    }
}