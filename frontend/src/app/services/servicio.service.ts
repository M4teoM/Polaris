import { Injectable } from '@angular/core';
import { Servicio } from '../models/servicio';
import { Testimonio } from '../models/testimonio';

@Injectable({
  providedIn: 'root',
})
export class ServicioService {
  private servicios: Servicio[] = [
    {
      id: 1,
      nombre: 'Spa & Wellness',
      descripcion:
        'Rejuvenezca su cuerpo y mente con nuestros servicios de spa de clase mundial',
      descripcionDetallada:
        'Disfrute de masajes terapéuticos, aromaterapia, baños termales y tratamientos faciales premium.',
      precio: 180000,
      imagenUrl:
        'https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800&q=80',
      categoria: 'Bienestar',
      icono: 'star',
    },
    {
      id: 2,
      nombre: 'Alta Cocina',
      descripcion:
        'Experimente la excelencia culinaria en nuestro restaurante galardonado',
      descripcionDetallada:
        'Nuestro chef ejecutivo crea menús de temporada con ingredientes locales y técnicas de vanguardia.',
      precio: 95000,
      imagenUrl:
        'https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&q=80',
      categoria: 'Gastronomía',
      icono: 'utensils',
    },
    {
      id: 3,
      nombre: 'Centro de Negocios',
      descripcion:
        'Instalaciones de última generación para el profesional moderno',
      descripcionDetallada:
        'Salas de reuniones equipadas con tecnología de punta, videoconferencia HD y espacios de coworking.',
      precio: 50000,
      imagenUrl:
        'https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80',
      categoria: 'Negocios',
      icono: 'briefcase',
    },
    {
      id: 4,
      nombre: 'Piscina & Fitness',
      descripcion: 'Piscina infinita y equipamiento deportivo premium',
      descripcionDetallada:
        'Piscina infinita con vista panorámica, gimnasio con equipos de última generación y clases de yoga.',
      precio: 45000,
      imagenUrl:
        'https://images.unsplash.com/photo-1576013551627-0cc20b96c2a7?w=800&q=80',
      categoria: 'Deporte',
      icono: 'swim',
    },
    {
      id: 5,
      nombre: 'Concierge',
      descripcion: 'Servicio personalizado 24/7 para todas sus necesidades',
      descripcionDetallada:
        'Nuestro equipo organiza reservas exclusivas, transporte de lujo, tours privados y solicitudes especiales.',
      precio: 0,
      imagenUrl:
        'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=800&q=80',
      categoria: 'Atención',
      icono: 'user',
    },
    {
      id: 6,
      nombre: 'Room Service',
      descripcion: 'Gastronomía gourmet entregada en su suite',
      descripcionDetallada:
        'Menú gourmet disponible las 24 horas con presentación impecable desde desayunos hasta cenas románticas.',
      precio: 35000,
      imagenUrl:
        'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80',
      categoria: 'Gastronomía',
      icono: 'coffee',
    },
    {
      id: 7,
      nombre: 'Traslado Privado',
      descripcion: 'Transporte ejecutivo desde y hacia el aeropuerto',
      descripcionDetallada:
        'Chofer privado, vehículo premium y asistencia personalizada para llegadas y salidas sin contratiempos.',
      precio: 120000,
      imagenUrl:
        'https://images.unsplash.com/photo-1511919884226-fd3cad34687c?w=800&q=80',
      categoria: 'Atención',
      icono: 'user',
    },
    {
      id: 8,
      nombre: 'Experiencia Romántica',
      descripcion: 'Decoración especial y cena privada para parejas',
      descripcionDetallada:
        'Incluye ambientación con velas, flores, cava y menú de tres tiempos en un espacio reservado.',
      precio: 240000,
      imagenUrl:
        'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=800&q=80',
      categoria: 'Experiencias',
      icono: 'star',
    },
    {
      id: 9,
      nombre: 'Tour Cultural',
      descripcion: 'Recorrido guiado por lugares emblemáticos de la ciudad',
      descripcionDetallada:
        'Visita a museos, centros históricos y miradores con guía bilingüe y transporte incluido.',
      precio: 160000,
      imagenUrl:
        'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80',
      categoria: 'Turismo',
      icono: 'briefcase',
    },
    {
      id: 10,
      nombre: 'Yoga al Amanecer',
      descripcion: 'Sesión guiada de bienestar frente al mar',
      descripcionDetallada:
        'Clase dirigida por instructor certificado, esterilla premium y bebidas naturales de cortesía.',
      precio: 70000,
      imagenUrl:
        'https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&q=80',
      categoria: 'Bienestar',
      icono: 'swim',
    },
    {
      id: 11,
      nombre: 'Masaje Relajante',
      descripcion: 'Terapia corporal para reducir estrés y fatiga',
      descripcionDetallada:
        'Sesión de 50 minutos con aceites esenciales y técnica de relajación profunda.',
      precio: 140000,
      imagenUrl:
        'https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800&q=80',
      categoria: 'Bienestar',
      icono: 'star',
    },
    {
      id: 12,
      nombre: 'Tratamiento Facial Deluxe',
      descripcion: 'Protocolo facial premium con resultados visibles',
      descripcionDetallada:
        'Limpieza profunda, hidratación intensiva y mascarilla regeneradora con productos de alta gama.',
      precio: 210000,
      imagenUrl:
        'https://images.unsplash.com/photo-1596178060671-7a58d1f5f4f1?w=800&q=80',
      categoria: 'Bienestar',
      icono: 'user',
    },
    {
      id: 13,
      nombre: 'Cata de Vinos',
      descripcion: 'Degustación guiada de etiquetas nacionales e importadas',
      descripcionDetallada:
        'Sommelier experto, maridaje de quesos y selección de vinos tintos, blancos y espumosos.',
      precio: 175000,
      imagenUrl:
        'https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?w=800&q=80',
      categoria: 'Gastronomía',
      icono: 'coffee',
    },
    {
      id: 14,
      nombre: 'Brunch de Autor',
      descripcion: 'Menú gourmet de media mañana con estación en vivo',
      descripcionDetallada:
        'Huevos al gusto, repostería artesanal, frutas frescas y bebidas de especialidad.',
      precio: 110000,
      imagenUrl:
        'https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=800&q=80',
      categoria: 'Gastronomía',
      icono: 'coffee',
    },
    {
      id: 15,
      nombre: 'Sala de Reuniones Premium',
      descripcion: 'Espacio corporativo para juntas privadas y presentaciones',
      descripcionDetallada:
        'Pantalla de proyección, café de bienvenida y soporte técnico durante la reunión.',
      precio: 90000,
      imagenUrl:
        'https://images.unsplash.com/photo-1497366754035-f200968a6e72?w=800&q=80',
      categoria: 'Negocios',
      icono: 'briefcase',
    },
    {
      id: 16,
      nombre: 'Coworking Ejecutivo',
      descripcion: 'Área de trabajo silenciosa con conectividad total',
      descripcionDetallada:
        'Escritorios ergonómicos, internet de alta velocidad y cabinas privadas para videollamadas.',
      precio: 65000,
      imagenUrl:
        'https://images.unsplash.com/photo-1524758631624-e2822e304c36?w=800&q=80',
      categoria: 'Negocios',
      icono: 'briefcase',
    },
    {
      id: 17,
      nombre: 'Clases de Natación',
      descripcion: 'Entrenamiento acuático personalizado',
      descripcionDetallada:
        'Instructor certificado, acompañamiento por niveles y acceso a piscina climatizada.',
      precio: 80000,
      imagenUrl:
        'https://images.unsplash.com/photo-1519315901367-f34ff9154487?w=800&q=80',
      categoria: 'Deporte',
      icono: 'swim',
    },
    {
      id: 18,
      nombre: 'Gimnasio 24/7',
      descripcion: 'Acceso ilimitado a zona fitness de última generación',
      descripcionDetallada:
        'Máquinas cardiovasculares, pesas libres, estación funcional y asesoría básica de entrenamiento.',
      precio: 40000,
      imagenUrl:
        'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800&q=80',
      categoria: 'Deporte',
      icono: 'swim',
    },
    {
      id: 19,
      nombre: 'Conserjería VIP',
      descripcion: 'Gestión personalizada de reservas y solicitudes especiales',
      descripcionDetallada:
        'Asistencia para restaurantes, entradas, transporte y experiencias a la medida durante la estadía.',
      precio: 0,
      imagenUrl:
        'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&q=80',
      categoria: 'Atención',
      icono: 'user',
    },
    {
      id: 20,
      nombre: 'Transporte Premium',
      descripcion: 'Servicio de movilidad confortable dentro de la ciudad',
      descripcionDetallada:
        'Traslados en vehículos ejecutivos con conductor profesional y horarios flexibles.',
      precio: 95000,
      imagenUrl:
        'https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&q=80',
      categoria: 'Turismo',
      icono: 'briefcase',
    },
  ];

  private testimonios: Testimonio[] = [
    {
      id: 1,
      texto:
        'Una experiencia absolutamente sublime. La atención al detalle y la excelencia en el servicio son inigualables.',
      autorNombre: 'Alexandra Chen',
      autorUbicacion: 'Singapur',
      estrellas: 5,
    },
    {
      id: 2,
      texto:
        'Desde el momento en que llegué, fui tratado como la realeza. Los servicios del spa son extraordinarios.',
      autorNombre: 'Marcus Wellington',
      autorUbicacion: 'Londres, UK',
      estrellas: 5,
    },
    {
      id: 3,
      texto:
        'La combinación perfecta de lujo moderno y elegancia atemporal. Cada aspecto superó mis expectativas.',
      autorNombre: 'Isabella Rodríguez',
      autorUbicacion: 'Barcelona, España',
      estrellas: 5,
    },
  ];

  /**
   * Retorna el catálogo completo de servicios del hotel.
   * @returns Arreglo de servicios.
   */
  getServicios(): Servicio[] {
    return [...this.servicios];
  }

  /**
   * Busca un servicio por su ID.
   * @param id ID del servicio.
   * @returns Servicio encontrado o undefined.
   */
  getServicioById(id: number): Servicio | undefined {
    return this.servicios.find((s) => s.id === id);
  }

  /**
   * Crea un nuevo servicio en el catálogo local.
   * @param servicio Datos del servicio a guardar.
   * @returns Servicio creado con id asignado.
   */
  create(servicio: Omit<Servicio, 'id'>): Servicio {
    const nuevoServicio: Servicio = {
      ...servicio,
      id: this.getNextId(),
    };

    this.servicios = [...this.servicios, nuevoServicio];
    return nuevoServicio;
  }

  /**
   * Actualiza un servicio existente dentro del catálogo local.
   * @param id Id del servicio a actualizar.
   * @param servicio Nuevos datos del servicio.
   * @returns True si encontró el servicio y lo actualizó.
   */
  update(id: number, servicio: Omit<Servicio, 'id'>): boolean {
    const index = this.servicios.findIndex((item) => item.id === id);

    if (index === -1) {
      return false;
    }

    this.servicios = this.servicios.map((item) =>
      item.id === id ? { ...servicio, id } : item,
    );

    return true;
  }

  /**
   * Elimina un servicio por id.
   * @param id Id del servicio a eliminar.
   * @returns True si el servicio existía.
   */
  delete(id: number): boolean {
    const existed = this.servicios.some((item) => item.id === id);
    this.servicios = this.servicios.filter((item) => item.id !== id);
    return existed;
  }

  /**
   * Filtra servicios por categoría.
   * @param categoria Categoría de servicio.
   * @returns Servicios pertenecientes a la categoría.
   */
  getServiciosByCategoria(categoria: string): Servicio[] {
    return this.servicios.filter((s) => s.categoria === categoria);
  }

  createServicio(servicio: Omit<Servicio, 'id'>): Servicio {
    const nextId =
      this.servicios.length > 0
        ? Math.max(...this.servicios.map((s) => s.id)) + 1
        : 1;

    const nuevoServicio: Servicio = { id: nextId, ...servicio };
    this.servicios = [...this.servicios, nuevoServicio];
    return nuevoServicio;
  }

  updateServicio(servicioActualizado: Servicio): Servicio | undefined {
    const index = this.servicios.findIndex((s) => s.id === servicioActualizado.id);
    if (index === -1) {
      return undefined;
    }

    this.servicios[index] = { ...servicioActualizado };
    return this.servicios[index];
  }

  deleteServicio(id: number): boolean {
    const totalAntes = this.servicios.length;
    this.servicios = this.servicios.filter((s) => s.id !== id);
    return this.servicios.length < totalAntes;
  }

  /**
   * Obtiene los testimonios usados en la sección pública.
   * @returns Arreglo de testimonios.
   */
  getTestimonios(): Testimonio[] {
    return this.testimonios;
  }

  private getNextId(): number {
    return this.servicios.length > 0
      ? Math.max(...this.servicios.map((servicio) => servicio.id)) + 1
      : 1;
  }
}
