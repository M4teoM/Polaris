import { Injectable } from '@angular/core';
import { Habitacion } from '../models/habitacion';

@Injectable({
  providedIn: 'root',
})
export class HabitacionService {
  private habitaciones: Habitacion[] = [
    {
      id: 1,
      nombre: 'Suite Presidencial',
      descripcion:
        'Nuestra suite más exclusiva con vista panorámica a la ciudad, sala de estar privada y servicio de mayordomo 24/7.',
      imagenUrl:
        'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800&q=80',
      precioPorNoche: 850000,
      metrosCuadrados: 120,
      tipoCama: 'King Size',
      capacidad: 4,
      disponible: true,
    },
    {
      id: 2,
      nombre: 'Suite Ejecutiva',
      descripcion:
        'Elegancia y funcionalidad perfecta para viajeros de negocios, con escritorio amplio y WiFi de alta velocidad.',
      imagenUrl:
        'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800&q=80',
      precioPorNoche: 520000,
      metrosCuadrados: 65,
      tipoCama: 'Queen Size',
      capacidad: 2,
      disponible: true,
    },
    {
      id: 3,
      nombre: 'Habitación Deluxe',
      descripcion:
        'Confort superior con decoración contemporánea y todas las comodidades modernas.',
      imagenUrl:
        'https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=800&q=80',
      precioPorNoche: 380000,
      metrosCuadrados: 45,
      tipoCama: 'Queen Size',
      capacidad: 2,
      disponible: true,
    },
    {
      id: 4,
      nombre: 'Habitación Estándar',
      descripcion:
        'Comodidad esencial en un ambiente acogedor con todas las amenidades necesarias.',
      imagenUrl:
        'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80',
      precioPorNoche: 250000,
      metrosCuadrados: 30,
      tipoCama: 'Doble',
      capacidad: 2,
      disponible: true,
    },
  ];

  /**
   * Obtiene el catálogo de habitaciones lógicas.
   * @returns Lista de habitaciones.
   */
  getHabitaciones(): Habitacion[] {
    return this.habitaciones;
  }

  /**
   * Busca una habitación por su ID.
   * @param id ID de la habitación.
   * @returns Habitación encontrada o undefined.
   */
  getHabitacionById(id: number): Habitacion | undefined {
    return this.habitaciones.find((h) => h.id === id);
  }

  /**
   * Formatea un precio para presentación en pesos colombianos.
   * @param price Valor numérico del precio.
   * @returns Precio formateado con símbolo monetario.
   */
  formatPrice(price: number): string {
    return '$' + price.toLocaleString('es-CO');
  }
}
