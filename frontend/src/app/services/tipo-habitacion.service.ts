import { Injectable } from '@angular/core';
import { TipoHabitacion } from '../models/tipo-habitacion';

@Injectable({
  providedIn: 'root',
})
export class TipoHabitacionService {
  private nextId = 5;

  private tiposHabitacion: TipoHabitacion[] = [
    {
      id: 1,
      nombre: 'Suite Presidencial',
      descripcion:
        'Nuestra suite más exclusiva con vista panorámica a la ciudad, sala de estar privada, jacuzzi y servicio de mayordomo 24/7.',
      imagenUrl:
        'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800&q=80',
      precioPorNoche: 850000,
      metrosCuadrados: 120,
      tipoCama: 'King Size',
      capacidad: 4,
    },
    {
      id: 2,
      nombre: 'Suite Ejecutiva',
      descripcion:
        'Elegancia y funcionalidad perfecta para viajeros de negocios, con escritorio amplio, sala de reuniones privada y WiFi de alta velocidad.',
      imagenUrl:
        'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800&q=80',
      precioPorNoche: 520000,
      metrosCuadrados: 65,
      tipoCama: 'Queen Size',
      capacidad: 2,
    },
    {
      id: 3,
      nombre: 'Habitación Deluxe',
      descripcion:
        'Confort superior con decoración contemporánea, balcón privado y todas las comodidades modernas para una estadía perfecta.',
      imagenUrl:
        'https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=800&q=80',
      precioPorNoche: 380000,
      metrosCuadrados: 45,
      tipoCama: 'Queen Size',
      capacidad: 2,
    },
    {
      id: 4,
      nombre: 'Habitación Estándar',
      descripcion:
        'Comodidad esencial en un ambiente acogedor con todas las amenidades necesarias para una estadía cómoda y relajante.',
      imagenUrl:
        'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80',
      precioPorNoche: 250000,
      metrosCuadrados: 30,
      tipoCama: 'Doble',
      capacidad: 2,
    },
  ];

  /**
   * Obtiene todos los tipos de habitación.
   * @returns Copia del arreglo de tipos de habitación.
   */
  getAll(): TipoHabitacion[] {
    return [...this.tiposHabitacion];
  }

  /**
   * Busca un tipo de habitación por ID.
   * @param id ID del tipo.
   * @returns Tipo encontrado o undefined.
   */
  getById(id: number): TipoHabitacion | undefined {
    return this.tiposHabitacion.find((t) => t.id === id);
  }

  /**
   * Crea un nuevo tipo de habitación asignando un ID incremental.
   * @param tipo Datos del tipo sin incluir ID.
   * @returns Tipo de habitación creado.
   */
  create(tipo: Omit<TipoHabitacion, 'id'>): TipoHabitacion {
    const nuevo: TipoHabitacion = { ...tipo, id: this.nextId++ };
    this.tiposHabitacion.push(nuevo);
    return nuevo;
  }

  /**
   * Actualiza un tipo de habitación existente por ID.
   * @param id ID del tipo a actualizar.
   * @param tipo Nuevos datos del tipo sin ID.
   * @returns Tipo actualizado o null si no existe.
   */
  update(id: number, tipo: Omit<TipoHabitacion, 'id'>): TipoHabitacion | null {
    const index = this.tiposHabitacion.findIndex((t) => t.id === id);
    if (index === -1) return null;
    this.tiposHabitacion[index] = { ...tipo, id };
    return this.tiposHabitacion[index];
  }

  /**
   * Elimina un tipo de habitación por ID.
   * @param id ID del tipo a eliminar.
   * @returns true si se eliminó correctamente; false si no existe.
   */
  delete(id: number): boolean {
    const index = this.tiposHabitacion.findIndex((t) => t.id === id);
    if (index === -1) return false;
    this.tiposHabitacion.splice(index, 1);
    return true;
  }

  /**
   * Formatea un precio para presentación en pesos colombianos.
   * @param price Precio numérico.
   * @returns Precio formateado.
   */
  formatPrice(price: number): string {
    return '$' + price.toLocaleString('es-CO');
  }
}
