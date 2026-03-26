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
    return this.servicios;
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
   * Filtra servicios por categoría.
   * @param categoria Categoría de servicio.
   * @returns Servicios pertenecientes a la categoría.
   */
  getServiciosByCategoria(categoria: string): Servicio[] {
    return this.servicios.filter((s) => s.categoria === categoria);
  }

  /**
   * Obtiene los testimonios usados en la sección pública.
   * @returns Arreglo de testimonios.
   */
  getTestimonios(): Testimonio[] {
    return this.testimonios;
  }
}
