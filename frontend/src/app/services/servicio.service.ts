import { Injectable } from '@angular/core';
import { Servicio } from '../models/servicio';
import { Testimonio } from '../models/testimonio';

@Injectable({
  providedIn: 'root'
})
export class ServicioService {

  private servicios: Servicio[] = [
    {
      id: 1,
      nombre: 'Spa & Wellness',
      descripcion: 'Rejuvenezca su cuerpo y mente con nuestros servicios de spa de clase mundial',
      descripcionDetallada: 'Disfrute de masajes terapéuticos, aromaterapia, baños termales y tratamientos faciales premium. Nuestro equipo de especialistas le ofrece una experiencia de relajación total en un ambiente de lujo incomparable.',
      imagenUrl: 'https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800&q=80',
      icono: 'star'
    },
    {
      id: 2,
      nombre: 'Alta Cocina',
      descripcion: 'Experimente la excelencia culinaria en nuestro restaurante galardonado',
      descripcionDetallada: 'Nuestro chef ejecutivo crea menús de temporada con ingredientes locales y técnicas de vanguardia. Maridajes exclusivos, cenas privadas y experiencias gastronómicas que deleitarán todos sus sentidos.',
      imagenUrl: 'https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&q=80',
      icono: 'utensils'
    },
    {
      id: 3,
      nombre: 'Centro de Negocios',
      descripcion: 'Instalaciones de última generación para el profesional moderno',
      descripcionDetallada: 'Salas de reuniones equipadas con tecnología de punta, videoconferencia HD, servicio de secretaría y espacios de coworking diseñados para maximizar su productividad en un entorno premium.',
      imagenUrl: 'https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80',
      icono: 'briefcase'
    },
    {
      id: 4,
      nombre: 'Piscina & Fitness',
      descripcion: 'Piscina infinita y equipamiento deportivo premium',
      descripcionDetallada: 'Nuestra piscina infinita con vista panorámica, gimnasio con equipos Technogym de última generación, clases de yoga al amanecer y entrenadores personales certificados a su disposición.',
      imagenUrl: 'https://images.unsplash.com/photo-1576013551627-0cc20b96c2a7?w=800&q=80',
      icono: 'swim'
    },
    {
      id: 5,
      nombre: 'Concierge',
      descripcion: 'Servicio personalizado 24/7 para todas sus necesidades',
      descripcionDetallada: 'Nuestro equipo de concierge está disponible las 24 horas para organizar reservas exclusivas, transporte de lujo, tours privados y cualquier solicitud especial que haga su estadía perfecta.',
      imagenUrl: 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=800&q=80',
      icono: 'user'
    },
    {
      id: 6,
      nombre: 'Room Service',
      descripcion: 'Gastronomía gourmet entregada en su suite',
      descripcionDetallada: 'Menú gourmet disponible las 24 horas con presentación impecable. Desde desayunos continentales hasta cenas románticas en la intimidad de su suite con la máxima calidad y atención al detalle.',
      imagenUrl: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80',
      icono: 'coffee'
    }
  ];

  private testimonios: Testimonio[] = [
    {
      id: 1,
      texto: 'Una experiencia absolutamente sublime. La atención al detalle y la excelencia en el servicio son inigualables.',
      autorNombre: 'Alexandra Chen',
      autorUbicacion: 'Singapur',
      estrellas: 5
    },
    {
      id: 2,
      texto: 'Desde el momento en que llegué, fui tratado como la realeza. Los servicios del spa son extraordinarios.',
      autorNombre: 'Marcus Wellington',
      autorUbicacion: 'Londres, UK',
      estrellas: 5
    },
    {
      id: 3,
      texto: 'La combinación perfecta de lujo moderno y elegancia atemporal. Cada aspecto de mi estadía superó las expectativas.',
      autorNombre: 'Isabella Rodríguez',
      autorUbicacion: 'Barcelona, España',
      estrellas: 5
    }
  ];

  getServicios(): Servicio[] {
    return this.servicios;
  }

  getServicioById(id: number): Servicio | undefined {
    return this.servicios.find(s => s.id === id);
  }

  getTestimonios(): Testimonio[] {
    return this.testimonios;
  }
}
