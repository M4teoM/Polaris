package com.polaris;

import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.Servicio;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import com.polaris.repository.IServicioRepository;
import com.polaris.repository.ITipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private ITipoHabitacionRepository tipoRepo;
    @Autowired private IHabitacionRepository habitacionRepo;
    @Autowired private IClienteRepository clienteRepo;
    @Autowired private IServicioRepository servicioRepo;
    @Autowired private IReservaHabitacionRepository reservaRepo;

    @Override
    @Transactional
    public void run(String... args) {

        boolean datosMinimosCargados = tipoRepo.count() >= 5
                && habitacionRepo.count() >= 5
                && clienteRepo.count() >= 5
                && servicioRepo.count() >= 5
                && reservaRepo.count() >= 5;

        if (datosMinimosCargados) {
            return;
        }

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

        for (int i = 1; i <= 10; i++)
            habitacionRepo.save(new Habitacion("10" + i, 1, estados[i % 3], estandar));

        for (int i = 1; i <= 5; i++)
            habitacionRepo.save(new Habitacion("20" + i, 2, estados[i % 3], estandar));
        for (int i = 6; i <= 10; i++)
            habitacionRepo.save(new Habitacion("20" + i, 2, estados[i % 3], ejecutiva));

        for (int i = 1; i <= 5; i++)
            habitacionRepo.save(new Habitacion("30" + i, 3, estados[i % 3], ejecutiva));
        for (int i = 6; i <= 10; i++)
            habitacionRepo.save(new Habitacion("30" + i, 3, estados[i % 3], vip));

        for (int i = 1; i <= 5; i++)
            habitacionRepo.save(new Habitacion("40" + i, 4, estados[i % 3], vip));
        for (int i = 6; i <= 10; i++)
            habitacionRepo.save(new Habitacion("40" + i, 4, estados[i % 3], familiar));

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

        // ── 20 Servicios ──────────────────────────────────────────────────
        Servicio s1 = new Servicio();
        s1.setNombre("Spa Premium");
        s1.setDescripcion("Experiencia completa de relajación");
        s1.setDescripcionDetallada("Nuestro Spa Premium ofrece una experiencia de relajación total. Incluye masajes terapéuticos, tratamientos faciales con productos de alta gama, aromaterapia, sauna finlandesa y jacuzzi privado.");
        s1.setPrecio(180000.0);
        s1.setImagenUrl("https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800&q=80");
        s1.setCategoria("Bienestar");
        s1.setDuracion("2 horas");
        s1.setHorario("Todos los días de 8:00 AM a 8:00 PM");
        s1.setIncluye("Masaje terapéutico de cuerpo completo|Tratamiento facial premium|Aromaterapia personalizada|Acceso a sauna finlandesa|Jacuzzi privado|Bata y sandalias de cortesía|Infusiones y snacks saludables");
        s1.setDestacados("Terapeutas certificados internacionalmente|Productos orgánicos de alta gama|Ambiente de tranquilidad absoluta|Reserva privada garantizada");
        servicioRepo.save(s1);

        Servicio s2 = new Servicio();
        s2.setNombre("Tour Histórico");
        s2.setDescripcion("Recorrido guiado por la ciudad");
        s2.setDescripcionDetallada("Un recorrido exclusivo por los lugares más emblemáticos e históricos de la ciudad. Nuestro guía bilingüe certificado le llevará a monumentos, museos y barrios históricos con anécdotas únicas.");
        s2.setPrecio(95000.0);
        s2.setImagenUrl("https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800&q=80");
        s2.setCategoria("Turismo");
        s2.setDuracion("4 horas");
        s2.setHorario("Lunes a sábado, salidas a las 9:00 AM y 2:00 PM");
        s2.setIncluye("Guía bilingüe certificado|Transporte privado con aire acondicionado|Refrigerios gourmet|Acceso a zonas restringidas|Seguro de viajero incluido|Fotografías del recorrido");
        s2.setDestacados("Recorrido por monumentos emblemáticos|Anécdotas históricas exclusivas|Grupos reducidos máx. 8 personas|Cancelación gratuita hasta 24h antes");
        servicioRepo.save(s2);

        Servicio s3 = new Servicio();
        s3.setNombre("Cena Gourmet");
        s3.setDescripcion("Menú degustación de cinco tiempos");
        s3.setDescripcionDetallada("Una experiencia gastronómica de alto nivel diseñada por nuestro chef ejecutivo con formación internacional. El menú de degustación de 5 tiempos fusiona lo mejor de la cocina local con técnicas de vanguardia.");
        s3.setPrecio(220000.0);
        s3.setImagenUrl("https://images.unsplash.com/photo-1551218808-94e220e084d2?w=800&q=80");
        s3.setCategoria("Gastronomía");
        s3.setDuracion("2.5 horas");
        s3.setHorario("Jueves a domingo, de 7:00 PM a 11:00 PM");
        s3.setIncluye("Menú degustación de 5 tiempos|Maridaje con vinos internacionales|Mesa privada con vista panorámica|Servicio de sommelier personalizado|Amuse-bouche de bienvenida|Petit fours de despedida");
        s3.setDestacados("Chef con formación internacional|Ingredientes de temporada y locales|Cocina de vanguardia|Ambiente íntimo y exclusivo");
        servicioRepo.save(s3);

        Servicio s4 = new Servicio();
        s4.setNombre("Aventura en Helicóptero");
        s4.setDescripcion("Vuelo panorámico sobre la ciudad");
        s4.setDescripcionDetallada("Disfrute de una experiencia exclusiva sobrevolando los principales paisajes urbanos y naturales de la región. Un recorrido aéreo con vistas que cortarán el aliento.");
        s4.setPrecio(450000.0);
        s4.setImagenUrl("https://images.unsplash.com/photo-1508615070457-7baeba4003ab?w=800&q=80");
        s4.setCategoria("Aventura");
        s4.setDuracion("45 minutos");
        s4.setHorario("Viernes a domingo, de 10:00 AM a 4:00 PM (sujeto a clima)");
        s4.setIncluye("Vuelo panorámico en helicóptero privado|Piloto certificado|Audioguía personalizada|Brindis premium al aterrizar|Seguro de vuelo completo|Video del vuelo en alta definición");
        s4.setDestacados("Vistas aéreas impresionantes|Experiencia VIP exclusiva|Seguridad certificada|Recuerdo en video incluido");
        servicioRepo.save(s4);

        Servicio s5 = new Servicio();
        s5.setNombre("Sesión Fotográfica Profesional");
        s5.setDescripcion("Captura momentos inolvidables");
        s5.setDescripcionDetallada("Sesión fotográfica profesional en locaciones icónicas del hotel y sus alrededores. Nuestro fotógrafo experto capturará los mejores momentos con técnicas de iluminación profesional.");
        s5.setPrecio(320000.0);
        s5.setImagenUrl("https://images.unsplash.com/photo-1519741497674-611481863552?w=800&q=80");
        s5.setCategoria("Experiencias");
        s5.setDuracion("1.5 horas");
        s5.setHorario("Todos los días, previa reserva con 24h de anticipación");
        s5.setIncluye("Fotógrafo profesional|25 fotografías editadas en alta resolución|Sesión en múltiples locaciones|Cambio de vestuario incluido|Galería digital privada|Impresión de foto favorita en canvas");
        s5.setDestacados("Portafolio de excelencia artística|Edición digital avanzada|Locaciones exclusivas del hotel|Entrega digital en 48 horas");
        servicioRepo.save(s5);

        Servicio s6 = new Servicio();
        s6.setNombre("Clase Privada de Cocina");
        s6.setDescripcion("Aprende con nuestro chef ejecutivo");
        s6.setDescripcionDetallada("Clase exclusiva donde aprenderá técnicas gourmet junto a nuestro chef ejecutivo. Una inmersión total en el arte culinario con ingredientes selectos y secretos profesionales.");
        s6.setPrecio(210000.0);
        s6.setImagenUrl("https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=800&q=80");
        s6.setCategoria("Gastronomía");
        s6.setDuracion("3 horas");
        s6.setHorario("Martes, jueves y sábado, de 10:00 AM a 1:00 PM");
        s6.setIncluye("Clase con chef ejecutivo|Ingredientes premium importados|Preparación de 3 platos gourmet|Degustación final con maridaje|Recetario exclusivo digital|Delantal de cortesía personalizado");
        s6.setDestacados("Aprendizaje práctico con expertos|Ingredientes de primera calidad|Recetas exclusivas del hotel|Experiencia gastronómica interactiva");
        servicioRepo.save(s6);

        Servicio s7 = new Servicio();
        s7.setNombre("Noche Romántica Deluxe");
        s7.setDescripcion("Decoración especial y amenidades premium");
        s7.setDescripcionDetallada("Transforme su estancia con una ambientación romántica inolvidable. Cada detalle ha sido cuidadosamente diseñado para crear una atmósfera mágica que celebre el amor.");
        s7.setPrecio(280000.0);
        s7.setImagenUrl("https://images.unsplash.com/photo-1511988617509-a57c8a288659?w=800&q=80");
        s7.setCategoria("Romántico");
        s7.setDuracion("Toda la noche");
        s7.setHorario("Disponible todos los días, preparación a partir de las 4:00 PM");
        s7.setIncluye("Decoración floral con pétalos de rosa|Velas aromáticas artesanales|Botella de champagne Moët & Chandon|Fresas cubiertas de chocolate belga|Desayuno especial en la habitación|Late checkout hasta las 2:00 PM");
        s7.setDestacados("Ambientación personalizada|Productos premium seleccionados|Atención al detalle impecable|Momento inolvidable garantizado");
        servicioRepo.save(s7);

        Servicio s8 = new Servicio();
        s8.setNombre("Ruta en Yate Privado");
        s8.setDescripcion("Experiencia náutica exclusiva");
        s8.setDescripcionDetallada("Navegue en un yate privado con tripulación certificada y servicio premium a bordo. Una experiencia náutica que combina la serenidad del mar con el lujo más exquisito.");
        s8.setPrecio(520000.0);
        s8.setImagenUrl("https://images.unsplash.com/photo-1500375592092-40eb2168fd21?w=800&q=80");
        s8.setCategoria("Lujo");
        s8.setDuracion("3 horas");
        s8.setHorario("Miércoles a domingo, salida a las 10:00 AM o 3:00 PM");
        s8.setIncluye("Yate privado con tripulación certificada|Bebidas premium ilimitadas|Snacks gourmet y tabla de mariscos|Equipo de snorkel incluido|Sistema de sonido Bluetooth|Seguro marítimo completo");
        s8.setDestacados("Tripulación profesional y certificada|Servicio todo incluido|Paisajes costeros exclusivos|Ideal para celebraciones VIP");
        servicioRepo.save(s8);

        Servicio s9 = new Servicio();
        s9.setNombre("Sesión de Yoga al Amanecer");
        s9.setDescripcion("Bienestar con vista panorámica");
        s9.setDescripcionDetallada("Clase privada de yoga guiada por instructor certificado en nuestra terraza panorámica. Una experiencia que armoniza cuerpo, mente y espíritu mientras contempla el amanecer más hermoso.");
        s9.setPrecio(120000.0);
        s9.setImagenUrl("https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&q=80");
        s9.setCategoria("Bienestar");
        s9.setDuracion("90 minutos");
        s9.setHorario("Todos los días, inicio a las 5:30 AM");
        s9.setIncluye("Clase privada con instructor certificado|Mat de yoga premium|Kit de bienestar aromático|Hidratación natural con agua infusionada|Desayuno saludable post-sesión|Meditación guiada al amanecer");
        s9.setDestacados("Vista panorámica del amanecer|Instructor con certificación internacional|Experiencia integral cuerpo-mente|Ambiente de paz absoluta");
        servicioRepo.save(s9);

        Servicio s10 = new Servicio();
        s10.setNombre("Experiencia Sommelier");
        s10.setDescripcion("Cata exclusiva de vinos internacionales");
        s10.setDescripcionDetallada("Degustación privada dirigida por sommelier profesional con selección de vinos internacionales premium. Un viaje sensorial a través de las mejores regiones vinícolas del mundo.");
        s10.setPrecio(260000.0);
        s10.setImagenUrl("https://images.unsplash.com/photo-1510626176961-4b57d4fbad03?w=800&q=80");
        s10.setCategoria("Gastronomía");
        s10.setDuracion("2 horas");
        s10.setHorario("Viernes y sábados, de 6:00 PM a 8:00 PM");
        s10.setIncluye("Cata de 8 vinos internacionales premium|Tabla de quesos artesanales|Explicación técnica de cada etiqueta|Copa de cristal de cortesía|Libreta de cata personalizada|Descuento en compra de botellas");
        s10.setDestacados("Sommelier con certificación WSET|Vinos de 5 regiones del mundo|Maridaje artesanal de alta cocina|Experiencia educativa y sensorial");
        servicioRepo.save(s10);

        Servicio s11 = new Servicio();
        s11.setNombre("Masaje de Piedras Calientes");
        s11.setDescripcion("Terapia ancestral de relajación profunda");
        s11.setDescripcionDetallada("Descubra el poder sanador de las piedras volcánicas calientes en una sesión que combina técnicas milenarias con la relajación más profunda, liberando tensiones y restaurando el equilibrio natural.");
        s11.setPrecio(150000.0);
        s11.setImagenUrl("https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?w=800&q=80");
        s11.setCategoria("Bienestar");
        s11.setDuracion("75 minutos");
        s11.setHorario("Todos los días de 9:00 AM a 7:00 PM");
        s11.setIncluye("Masaje con piedras volcánicas naturales|Aceites esenciales orgánicos|Música de relajación personalizada|Infusión detox post-sesión|Acceso a zona de relajación 30 min extra");
        s11.setDestacados("Técnica terapéutica milenaria|Piedras volcánicas de origen certificado|Terapeuta especializado en técnicas orientales|Liberación profunda de tensiones");
        servicioRepo.save(s11);

        Servicio s12 = new Servicio();
        s12.setNombre("Excursión de Buceo");
        s12.setDescripcion("Descubre el mundo submarino");
        s12.setDescripcionDetallada("Sumérjase en una aventura submarina guiada por instructores PADI certificados. Explore arrecifes de coral vibrantes y nade junto a peces tropicales en una experiencia inolvidable.");
        s12.setPrecio(380000.0);
        s12.setImagenUrl("https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&q=80");
        s12.setCategoria("Aventura");
        s12.setDuracion("4 horas");
        s12.setHorario("Martes, jueves y sábado, salida a las 8:00 AM");
        s12.setIncluye("Instructor PADI certificado|Equipo completo de buceo profesional|Dos inmersiones en arrecifes distintos|Transporte marítimo ida y vuelta|Snacks y bebidas a bordo|Fotografías submarinas incluidas|Seguro de buceo completo");
        s12.setDestacados("Arrecifes de coral prístinos|Apto para todos los niveles|Equipo de última generación|Máximo 6 personas por grupo");
        servicioRepo.save(s12);

        Servicio s13 = new Servicio();
        s13.setNombre("Servicio de Limusina");
        s13.setDescripcion("Transporte de lujo premium");
        s13.setDescripcionDetallada("Viaje con la máxima distinción en nuestra flota de limusinas de última generación. Servicio ejecutivo con chofer profesional para traslados, eventos especiales o recorridos nocturnos.");
        s13.setPrecio(350000.0);
        s13.setImagenUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80");
        s13.setCategoria("Lujo");
        s13.setDuracion("Hasta 5 horas");
        s13.setHorario("Disponible 24/7, reserva con 12h de anticipación");
        s13.setIncluye("Limusina de última generación|Chofer profesional bilingüe|Bebidas premium a bordo|WiFi y sistema multimedia|Periódicos y revistas internacionales|Climatización personalizada");
        s13.setDestacados("Disponibilidad 24/7|Flota impecable y moderna|Chofer profesional uniformado|Máxima puntualidad garantizada");
        servicioRepo.save(s13);

        Servicio s14 = new Servicio();
        s14.setNombre("Taller de Mixología");
        s14.setDescripcion("Crea cócteles como un experto");
        s14.setDescripcionDetallada("Aprenda el arte de la coctelería de la mano de nuestro bartender premiado. Desde los clásicos atemporales hasta creaciones de autor, dominará técnicas profesionales de mezcla y presentación.");
        s14.setPrecio(175000.0);
        s14.setImagenUrl("https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?w=800&q=80");
        s14.setCategoria("Gastronomía");
        s14.setDuracion("2 horas");
        s14.setHorario("Miércoles y viernes, de 5:00 PM a 7:00 PM");
        s14.setIncluye("Clase con bartender premiado internacionalmente|Preparación de 5 cócteles diferentes|Ingredientes premium importados|Kit de coctelería de cortesía|Recetario digital exclusivo|Degustación de creaciones propias");
        s14.setDestacados("Bartender con premios internacionales|Técnicas profesionales de barra|Ingredientes y licores premium|Experiencia divertida y educativa");
        servicioRepo.save(s14);

        Servicio s15 = new Servicio();
        s15.setNombre("Observación de Estrellas");
        s15.setDescripcion("Astronomía bajo el cielo nocturno");
        s15.setDescripcionDetallada("Una experiencia celestial única en nuestra terraza astronómica equipada con telescopios profesionales. Guiado por un astrónomo experto, descubra constelaciones, planetas y nebulosas.");
        s15.setPrecio(140000.0);
        s15.setImagenUrl("https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&q=80");
        s15.setCategoria("Experiencias");
        s15.setDuracion("2 horas");
        s15.setHorario("Noches despejadas, de 8:00 PM a 10:00 PM");
        s15.setIncluye("Telescopio profesional Celestron|Guía astrónomo certificado|Mapa estelar personalizado|Chocolate caliente gourmet y snacks|Mantas y sillas reclinables|Fotografía astral de recuerdo");
        s15.setDestacados("Terraza astronómica exclusiva|Cielo con mínima contaminación lumínica|Guía experto en astronomía|Experiencia mágica e irrepetible");
        servicioRepo.save(s15);

        Servicio s16 = new Servicio();
        s16.setNombre("Golf Premium");
        s16.setDescripcion("Experiencia de golf de clase mundial");
        s16.setDescripcionDetallada("Disfrute de una jornada de golf en un campo de campeonato de 18 hoyos diseñado por arquitectos de renombre mundial. Fairways impecables, greens de competición y vistas espectaculares.");
        s16.setPrecio(290000.0);
        s16.setImagenUrl("https://images.unsplash.com/photo-1535131749006-b7f58c99034b?w=800&q=80");
        s16.setCategoria("Deportes");
        s16.setDuracion("5 horas");
        s16.setHorario("Todos los días de 6:00 AM a 6:00 PM");
        s16.setIncluye("Green fee para 18 hoyos de campeonato|Carrito de golf eléctrico|Caddie profesional bilingüe|Set de palos premium disponibles|Almuerzo en el clubhouse|Clase introductoria para principiantes|Acceso a driving range");
        s16.setDestacados("Campo diseñado por arquitecto de renombre|Greens de competición profesional|Vistas panorámicas espectaculares|Clubhouse con todas las comodidades");
        servicioRepo.save(s16);

        Servicio s17 = new Servicio();
        s17.setNombre("Tratamiento Facial Deluxe");
        s17.setDescripcion("Rejuvenecimiento facial avanzado");
        s17.setDescripcionDetallada("Tratamiento facial de última generación que combina tecnología de punta con ingredientes naturales premium. Protocolo único que hidrata, ilumina y rejuvenece visiblemente.");
        s17.setPrecio(200000.0);
        s17.setImagenUrl("https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=800&q=80");
        s17.setCategoria("Bienestar");
        s17.setDuracion("90 minutos");
        s17.setHorario("Todos los días de 9:00 AM a 6:00 PM");
        s17.setIncluye("Análisis de piel con tecnología avanzada|Limpieza profunda con ultrasonido|Mascarilla de oro 24K|Sérum de ácido hialurónico puro|Masaje facial lifting|Protector solar premium SPF50|Crema hidratante de regalo");
        s17.setDestacados("Tecnología dermocosmética de vanguardia|Productos con oro 24K|Protocolo personalizado por especialista|Resultados visibles inmediatos");
        servicioRepo.save(s17);

        Servicio s18 = new Servicio();
        s18.setNombre("Tour Gastronómico");
        s18.setDescripcion("Sabores auténticos de la región");
        s18.setDescripcionDetallada("Un recorrido culinario por los mercados, restaurantes y puestos más auténticos de la ciudad. Descubra la gastronomía local degustando platos típicos e ingredientes exóticos.");
        s18.setPrecio(160000.0);
        s18.setImagenUrl("https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=80");
        s18.setCategoria("Gastronomía");
        s18.setDuracion("3.5 horas");
        s18.setHorario("Lunes, miércoles y viernes, salida a las 10:00 AM");
        s18.setIncluye("Guía foodie experto local|Degustación en 6 paradas gastronómicas|Mercado tradicional con ingredientes exóticos|Bebidas locales artesanales|Transporte entre paradas|Mapa gastronómico personalizado");
        s18.setDestacados("Experiencia auténtica y local|Sabores que no encontrarás en restaurantes|Grupos íntimos máx. 10 personas|Historia y cultura a través de la comida");
        servicioRepo.save(s18);

        Servicio s19 = new Servicio();
        s19.setNombre("Safari Fotográfico");
        s19.setDescripcion("Aventura visual en la naturaleza");
        s19.setDescripcionDetallada("Expedición fotográfica guiada por escenarios naturales impresionantes. Acompañado por un fotógrafo de naturaleza profesional, aprenderá técnicas de composición y captura de fauna y flora.");
        s19.setPrecio(250000.0);
        s19.setImagenUrl("https://images.unsplash.com/photo-1516426122078-c23e76319801?w=800&q=80");
        s19.setCategoria("Aventura");
        s19.setDuracion("5 horas");
        s19.setHorario("Martes y sábado, salida a las 6:00 AM");
        s19.setIncluye("Fotógrafo de naturaleza profesional|Transporte 4x4 todoterreno|Equipo fotográfico disponible|Desayuno de campo gourmet|Guía de fauna y flora local|10 fotografías editadas profesionalmente|Chaleco y sombrero safari de cortesía");
        s19.setDestacados("Escenarios naturales vírgenes|Guía fotógrafo galardonado|Avistamiento de fauna silvestre|Aventura y aprendizaje artístico");
        servicioRepo.save(s19);

        Servicio s20 = new Servicio();
        s20.setNombre("Meditación y Mindfulness");
        s20.setDescripcion("Paz interior y conexión profunda");
        s20.setDescripcionDetallada("Retiro de meditación guiada en nuestro jardín zen privado. Nuestro maestro con más de 20 años de experiencia le guiará a través de técnicas de respiración, visualización y mindfulness.");
        s20.setPrecio(130000.0);
        s20.setImagenUrl("https://images.unsplash.com/photo-1528715471579-d1bcf0ba5e83?w=800&q=80");
        s20.setCategoria("Bienestar");
        s20.setDuracion("2 horas");
        s20.setHorario("Todos los días, sesiones a las 7:00 AM y 5:00 PM");
        s20.setIncluye("Sesión guiada por maestro certificado|Jardín zen privado exclusivo|Técnicas de respiración profunda|Meditación guiada con visualización|Kit de aromaterapia|Té ceremonial japonés|Guía de meditación digital para continuar en casa");
        s20.setDestacados("Maestro con 20+ años de experiencia|Jardín zen diseñado para la paz|Técnicas ancestrales y modernas|Transformación interior duradera");
        servicioRepo.save(s20);

        // ── 8 Reservas de Habitación ──────────────────────────────────────
        // Se recuperan los clientes y habitaciones ya persistidos
        List<Cliente> clientes = clienteRepo.findAll();
        List<Habitacion> habitaciones = habitacionRepo.findAll();

        // Reserva 1 – Confirmada (cliente 0, habitación 0)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 5),
            "Confirmada", 2, clientes.get(0), habitaciones.get(0)));

        // Reserva 2 – Confirmada (cliente 1, habitación 1)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 14),
            "Confirmada", 1, clientes.get(1), habitaciones.get(1)));

        // Reserva 3 – Pendiente (cliente 2, habitación 2)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 7),
            "Pendiente", 3, clientes.get(2), habitaciones.get(2)));

        // Reserva 4 – Finalizada (cliente 3, habitación 3)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 4),
            "Finalizada", 2, clientes.get(3), habitaciones.get(3)));

        // Reserva 5 – Cancelada (cliente 4, habitación 4)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 12),
            "Cancelada", 1, clientes.get(4), habitaciones.get(4)));

        // Reserva 6 – Segunda reserva del cliente 0 en distinta habitación (relación ManyToOne)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 6, 15), LocalDate.of(2026, 6, 20),
            "Confirmada", 2, clientes.get(0), habitaciones.get(10)));

        // Reserva 7 – Mismo cliente 1, otra habitación (verifica ManyToOne)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3),
            "Pendiente", 1, clientes.get(1), habitaciones.get(15)));

        // Reserva 8 – Misma habitación reservada en fecha distinta (verifica ManyToOne)
        reservaRepo.save(new ReservaHabitacion(
            LocalDate.of(2026, 8, 5), LocalDate.of(2026, 8, 10),
            "Confirmada", 4, clientes.get(5), habitaciones.get(0)));

    }
}