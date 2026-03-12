package com.polaris;

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

        // ── Servicios (20) ─────────────────────────────────────────────
        servicioRepo.save(new Servicio(
            "Spa Premium", "Experiencia completa de relajación",
            "Nuestro Spa Premium ofrece una experiencia de relajación total. Incluye masajes terapéuticos, tratamientos faciales con productos de alta gama, aromaterapia, sauna finlandesa y jacuzzi privado. Nuestros terapeutas certificados le brindarán atención personalizada de puro bienestar.",
            180000.0, "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800&q=80", "Bienestar",
            "2 horas", "Todos los días de 8:00 AM a 8:00 PM",
            "Masaje terapéutico de cuerpo completo|Tratamiento facial premium|Aromaterapia personalizada|Acceso a sauna finlandesa|Jacuzzi privado|Bata y sandalias de cortesía|Infusiones y snacks saludables",
            "Terapeutas certificados internacionalmente|Productos orgánicos de alta gama|Ambiente de tranquilidad absoluta|Reserva privada garantizada"));

        servicioRepo.save(new Servicio(
            "Tour Histórico", "Recorrido guiado por la ciudad",
            "Un recorrido exclusivo por los lugares más emblemáticos e históricos de la ciudad. Nuestro guía bilingüe certificado le llevará a monumentos, museos y barrios históricos con anécdotas únicas.",
            95000.0, "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800&q=80", "Turismo",
            "4 horas", "Lunes a sábado, salidas a las 9:00 AM y 2:00 PM",
            "Guía bilingüe certificado|Transporte privado con aire acondicionado|Refrigerios gourmet durante el recorrido|Acceso a zonas restringidas|Seguro de viajero incluido|Fotografías del recorrido",
            "Recorrido por monumentos emblemáticos|Anécdotas históricas exclusivas|Grupos reducidos máx. 8 personas|Cancelación gratuita hasta 24h antes"));

        servicioRepo.save(new Servicio(
            "Cena Gourmet", "Menú degustación de cinco tiempos",
            "Una experiencia gastronómica de alto nivel diseñada por nuestro chef ejecutivo con formación internacional. El menú de degustación de 5 tiempos fusiona lo mejor de la cocina local con técnicas de vanguardia.",
            220000.0, "https://images.unsplash.com/photo-1551218808-94e220e084d2?w=800&q=80", "Gastronomía",
            "2.5 horas", "Jueves a domingo, de 7:00 PM a 11:00 PM",
            "Menú degustación de 5 tiempos|Maridaje con vinos internacionales|Mesa privada con vista panorámica|Servicio de sommelier personalizado|Amuse-bouche de bienvenida|Petit fours de despedida",
            "Chef con formación internacional|Ingredientes de temporada y locales|Cocina de vanguardia y técnicas moleculares|Ambiente íntimo y exclusivo"));

        servicioRepo.save(new Servicio(
            "Aventura en Helicóptero", "Vuelo panorámico sobre la ciudad",
            "Disfrute de una experiencia exclusiva sobrevolando los principales paisajes urbanos y naturales de la región. Un recorrido aéreo con vistas que cortarán el aliento y momentos que perdurarán para siempre.",
            450000.0, "https://images.unsplash.com/photo-1508615070457-7baeba4003ab?w=800&q=80", "Aventura",
            "45 minutos", "Viernes a domingo, de 10:00 AM a 4:00 PM (sujeto a clima)",
            "Vuelo panorámico en helicóptero privado|Piloto certificado con experiencia|Audioguía personalizada del recorrido|Brindis premium al aterrizar|Seguro de vuelo completo|Video del vuelo en alta definición",
            "Vistas aéreas impresionantes|Experiencia VIP exclusiva|Seguridad certificada|Recuerdo en video incluido"));

        servicioRepo.save(new Servicio(
            "Sesión Fotográfica Profesional", "Captura momentos inolvidables",
            "Sesión fotográfica profesional en locaciones icónicas del hotel y sus alrededores. Nuestro fotógrafo experto capturará los mejores momentos con técnicas de iluminación profesional y composición artística.",
            320000.0, "https://images.unsplash.com/photo-1519741497674-611481863552?w=800&q=80", "Experiencias",
            "1.5 horas", "Todos los días, previa reserva con 24h de anticipación",
            "Fotógrafo profesional con portafolio premium|25 fotografías en alta resolución editadas|Sesión en múltiples locaciones|Cambio de vestuario incluido|Galería digital privada|Impresión de foto favorita en canvas",
            "Portafolio de excelencia artística|Edición digital avanzada|Locaciones exclusivas del hotel|Entrega digital en 48 horas"));

        servicioRepo.save(new Servicio(
            "Clase Privada de Cocina", "Aprende con nuestro chef ejecutivo",
            "Clase exclusiva donde aprenderá técnicas gourmet junto a nuestro chef ejecutivo. Una inmersión total en el arte culinario con ingredientes selectos y secretos profesionales.",
            210000.0, "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=800&q=80", "Gastronomía",
            "3 horas", "Martes, jueves y sábado, de 10:00 AM a 1:00 PM",
            "Clase con chef ejecutivo del hotel|Ingredientes premium importados|Preparación de 3 platos gourmet|Degustación final con maridaje|Recetario exclusivo digital|Delantal de cortesía personalizado",
            "Aprendizaje práctico con expertos|Ingredientes de primera calidad|Recetas exclusivas del hotel|Experiencia gastronómica interactiva"));

        servicioRepo.save(new Servicio(
            "Noche Romántica Deluxe", "Decoración especial y amenidades premium",
            "Transforme su estancia con una ambientación romántica inolvidable. Cada detalle ha sido cuidadosamente diseñado para crear una atmósfera mágica que celebre el amor y la conexión especial.",
            280000.0, "https://images.unsplash.com/photo-1511988617509-a57c8a288659?w=800&q=80", "Romántico",
            "Toda la noche", "Disponible todos los días, preparación a partir de las 4:00 PM",
            "Decoración floral con pétalos de rosa|Velas aromáticas artesanales|Botella de champagne Moët & Chandon|Fresas cubiertas de chocolate belga|Desayuno especial en la habitación|Música ambiental personalizada|Late checkout hasta las 2:00 PM",
            "Ambientación personalizada|Productos premium seleccionados|Atención al detalle impecable|Momento inolvidable garantizado"));

        servicioRepo.save(new Servicio(
            "Ruta en Yate Privado", "Experiencia náutica exclusiva",
            "Navegue en un yate privado con tripulación certificada y servicio premium a bordo. Una experiencia náutica que combina la serenidad del mar con el lujo más exquisito.",
            520000.0, "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?w=800&q=80", "Lujo",
            "3 horas", "Miércoles a domingo, salida a las 10:00 AM o 3:00 PM",
            "Yate privado con tripulación certificada|Servicio de bebidas premium ilimitadas|Snacks gourmet y tabla de mariscos|Equipo de snorkel incluido|Sistema de sonido Bluetooth|Parada para baño en cala privada|Seguro marítimo completo",
            "Tripulación profesional y certificada|Servicio todo incluido|Paisajes costeros exclusivos|Ideal para celebraciones VIP"));

        servicioRepo.save(new Servicio(
            "Sesión de Yoga al Amanecer", "Bienestar con vista panorámica",
            "Clase privada de yoga guiada por instructor certificado en nuestra terraza panorámica. Una experiencia que armoniza cuerpo, mente y espíritu mientras contempla el amanecer más hermoso.",
            120000.0, "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&q=80", "Bienestar",
            "90 minutos", "Todos los días, inicio a las 5:30 AM",
            "Clase privada con instructor certificado|Mat de yoga premium|Kit de bienestar aromático|Hidratación natural con agua infusionada|Desayuno saludable post-sesión|Meditación guiada al amanecer",
            "Vista panorámica del amanecer|Instructor con certificación internacional|Experiencia integral cuerpo-mente|Ambiente de paz absoluta"));

        servicioRepo.save(new Servicio(
            "Experiencia Sommelier", "Cata exclusiva de vinos internacionales",
            "Degustación privada dirigida por sommelier profesional con selección de vinos internacionales premium. Un viaje sensorial a través de las mejores regiones vinícolas del mundo.",
            260000.0, "https://images.unsplash.com/photo-1510626176961-4b57d4fbad03?w=800&q=80", "Gastronomía",
            "2 horas", "Viernes y sábados, de 6:00 PM a 8:00 PM",
            "Cata de 8 vinos internacionales premium|Tabla de quesos artesanales seleccionados|Explicación técnica de cada etiqueta|Copa de cristal de cortesía|Libreta de cata personalizada|Descuento en compra de botellas",
            "Sommelier con certificación WSET|Vinos de 5 regiones del mundo|Maridaje artesanal de alta cocina|Experiencia educativa y sensorial"));

        servicioRepo.save(new Servicio(
            "Masaje de Piedras Calientes", "Terapia ancestral de relajación profunda",
            "Descubra el poder sanador de las piedras volcánicas calientes en una sesión que combina técnicas milenarias con la relajación más profunda, liberando tensiones y restaurando el equilibrio natural.",
            150000.0, "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?w=800&q=80", "Bienestar",
            "75 minutos", "Todos los días de 9:00 AM a 7:00 PM",
            "Masaje con piedras volcánicas naturales|Aceites esenciales orgánicos|Música de relajación personalizada|Infusión detox post-sesión|Acceso a zona de relajación 30 min extra",
            "Técnica terapéutica milenaria|Piedras volcánicas de origen certificado|Terapeuta especializado en técnicas orientales|Liberación profunda de tensiones"));

        servicioRepo.save(new Servicio(
            "Excursión de Buceo", "Descubre el mundo submarino",
            "Sumérjase en una aventura submarina guiada por instructores PADI certificados. Explore arrecifes de coral vibrantes y nade junto a peces tropicales en una experiencia inolvidable.",
            380000.0, "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&q=80", "Aventura",
            "4 horas", "Martes, jueves y sábado, salida a las 8:00 AM",
            "Instructor PADI certificado|Equipo completo de buceo profesional|Dos inmersiones en arrecifes distintos|Transporte marítimo ida y vuelta|Snacks y bebidas a bordo|Fotografías submarinas incluidas|Seguro de buceo completo",
            "Arrecifes de coral prístinos|Apto para todos los niveles|Equipo de última generación|Máximo 6 personas por grupo"));

        servicioRepo.save(new Servicio(
            "Servicio de Limusina", "Transporte de lujo premium",
            "Viaje con la máxima distinción en nuestra flota de limusinas de última generación. Servicio ejecutivo con chofer profesional para traslados, eventos especiales o recorridos nocturnos.",
            350000.0, "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80", "Lujo",
            "Hasta 5 horas", "Disponible 24/7, reserva con 12h de anticipación",
            "Limusina de última generación|Chofer profesional bilingüe|Bebidas premium a bordo|WiFi y sistema multimedia|Periódicos y revistas internacionales|Climatización personalizada",
            "Disponibilidad 24/7|Flota impecable y moderna|Chofer profesional uniformado|Máxima puntualidad garantizada"));

        servicioRepo.save(new Servicio(
            "Taller de Mixología", "Crea cócteles como un experto",
            "Aprenda el arte de la coctelería de la mano de nuestro bartender premiado. Desde los clásicos atemporales hasta creaciones de autor, dominará técnicas profesionales de mezcla y presentación.",
            175000.0, "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?w=800&q=80", "Gastronomía",
            "2 horas", "Miércoles y viernes, de 5:00 PM a 7:00 PM",
            "Clase con bartender premiado internacionalmente|Preparación de 5 cócteles diferentes|Ingredientes premium importados|Kit de coctelería de cortesía|Recetario digital exclusivo|Degustación de creaciones propias",
            "Bartender con premios internacionales|Técnicas profesionales de barra|Ingredientes y licores premium|Experiencia divertida y educativa"));

        servicioRepo.save(new Servicio(
            "Observación de Estrellas", "Astronomía bajo el cielo nocturno",
            "Una experiencia celestial única en nuestra terraza astronómica equipada con telescopios profesionales. Guiado por un astrónomo experto, descubra constelaciones, planetas y nebulosas.",
            140000.0, "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&q=80", "Experiencias",
            "2 horas", "Noches despejadas, de 8:00 PM a 10:00 PM",
            "Telescopio profesional Celestron|Guía astrónomo certificado|Mapa estelar personalizado|Chocolate caliente gourmet y snacks|Mantas y sillas reclinables|Fotografía astral de recuerdo",
            "Terraza astronómica exclusiva|Cielo con mínima contaminación lumínica|Guía experto en astronomía|Experiencia mágica e irrepetible"));

        servicioRepo.save(new Servicio(
            "Golf Premium", "Experiencia de golf de clase mundial",
            "Disfrute de una jornada de golf en un campo de campeonato de 18 hoyos diseñado por arquitectos de renombre mundial. Fairways impecables, greens de competición y vistas espectaculares.",
            290000.0, "https://images.unsplash.com/photo-1535131749006-b7f58c99034b?w=800&q=80", "Deportes",
            "5 horas", "Todos los días de 6:00 AM a 6:00 PM",
            "Green fee para 18 hoyos de campeonato|Carrito de golf eléctrico|Caddie profesional bilingüe|Set de palos premium disponibles|Almuerzo en el clubhouse|Clase introductoria para principiantes|Acceso a driving range",
            "Campo diseñado por arquitecto de renombre|Greens de competición profesional|Vistas panorámicas espectaculares|Clubhouse con todas las comodidades"));

        servicioRepo.save(new Servicio(
            "Tratamiento Facial Deluxe", "Rejuvenecimiento facial avanzado",
            "Tratamiento facial de última generación que combina tecnología de punta con ingredientes naturales premium. Protocolo único que hidrata, ilumina y rejuvenece visiblemente.",
            200000.0, "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=800&q=80", "Bienestar",
            "90 minutos", "Todos los días de 9:00 AM a 6:00 PM",
            "Análisis de piel con tecnología avanzada|Limpieza profunda con ultrasonido|Mascarilla de oro 24K|Sérum de ácido hialurónico puro|Masaje facial lifting|Protector solar premium SPF50|Crema hidratante de regalo",
            "Tecnología dermocosmética de vanguardia|Productos con oro 24K|Protocolo personalizado por especialista|Resultados visibles inmediatos"));

        servicioRepo.save(new Servicio(
            "Tour Gastronómico", "Sabores auténticos de la región",
            "Un recorrido culinario por los mercados, restaurantes y puestos más auténticos de la ciudad. Descubra la gastronomía local degustando platos típicos e ingredientes exóticos.",
            160000.0, "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=80", "Gastronomía",
            "3.5 horas", "Lunes, miércoles y viernes, salida a las 10:00 AM",
            "Guía foodie experto local|Degustación en 6 paradas gastronómicas|Mercado tradicional con ingredientes exóticos|Bebidas locales artesanales|Transporte entre paradas|Mapa gastronómico personalizado",
            "Experiencia auténtica y local|Sabores que no encontrarás en restaurantes|Grupos íntimos máx. 10 personas|Historia y cultura a través de la comida"));

        servicioRepo.save(new Servicio(
            "Safari Fotográfico", "Aventura visual en la naturaleza",
            "Expedición fotográfica guiada por escenarios naturales impresionantes. Acompañado por un fotógrafo de naturaleza profesional, aprenderá técnicas de composición y captura de fauna y flora.",
            250000.0, "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=800&q=80", "Aventura",
            "5 horas", "Martes y sábado, salida a las 6:00 AM",
            "Fotógrafo de naturaleza profesional|Transporte 4x4 todoterreno|Equipo fotográfico disponible|Desayuno de campo gourmet|Guía de fauna y flora local|10 fotografías editadas profesionalmente|Chaleco y sombrero safari de cortesía",
            "Escenarios naturales vírgenes|Guía fotógrafo galardonado|Avistamiento de fauna silvestre|Aventura y aprendizaje artístico"));

        servicioRepo.save(new Servicio(
            "Meditación y Mindfulness", "Paz interior y conexión profunda",
            "Retiro de meditación guiada en nuestro jardín zen privado. Nuestro maestro con más de 20 años de experiencia le guiará a través de técnicas de respiración, visualización y mindfulness.",
            130000.0, "https://images.unsplash.com/photo-1528715471579-d1bcf0ba5e83?w=800&q=80", "Bienestar",
            "2 horas", "Todos los días, sesiones a las 7:00 AM y 5:00 PM",
            "Sesión guiada por maestro certificado|Jardín zen privado exclusivo|Técnicas de respiración profunda|Meditación guiada con visualización|Kit de aromaterapia|Té ceremonial japonés|Guía de meditación digital para continuar en casa",
            "Maestro con 20+ años de experiencia|Jardín zen diseñado para la paz|Técnicas ancestrales y modernas|Transformación interior duradera"));

        System.out.println("✅ Datos insertados: 5 tipos, 50 habitaciones, 10 clientes, 20 servicios");
    }
}