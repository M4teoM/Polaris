import { Injectable } from '@angular/core';
import { HabitacionFisica } from '../models/habitacion-fisica';

@Injectable({
  providedIn: 'root',
})
export class HabitacionFisicaService {
  private habitaciones: HabitacionFisica[] = [
    // Suite Presidencial (tipo 1)
    {
      id: 1,
      numero: '501',
      piso: 5,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 1,
    },
    { id: 2, numero: '502', piso: 5, estado: 'OCUPADA', tipoHabitacionId: 1 },

    // Suite Ejecutiva (tipo 2)
    {
      id: 3,
      numero: '401',
      piso: 4,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 2,
    },
    {
      id: 4,
      numero: '402',
      piso: 4,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 2,
    },
    {
      id: 5,
      numero: '403',
      piso: 4,
      estado: 'MANTENIMIENTO',
      tipoHabitacionId: 2,
    },

    // Habitación Deluxe (tipo 3)
    {
      id: 6,
      numero: '301',
      piso: 3,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 3,
    },
    { id: 7, numero: '302', piso: 3, estado: 'OCUPADA', tipoHabitacionId: 3 },
    {
      id: 8,
      numero: '303',
      piso: 3,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 3,
    },
    { id: 9, numero: '304', piso: 3, estado: 'LIMPIEZA', tipoHabitacionId: 3 },

    // Habitación Estándar (tipo 4)
    {
      id: 10,
      numero: '201',
      piso: 2,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 4,
    },
    {
      id: 11,
      numero: '202',
      piso: 2,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 4,
    },
    { id: 12, numero: '203', piso: 2, estado: 'OCUPADA', tipoHabitacionId: 4 },
    {
      id: 13,
      numero: '204',
      piso: 2,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 4,
    },
    {
      id: 14,
      numero: '101',
      piso: 1,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 4,
    },
    {
      id: 15,
      numero: '102',
      piso: 1,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 4,
    },
  ];

  /**
   * Devuelve todas las habitaciones físicas registradas.
   * @returns Arreglo de habitaciones físicas.
   */
  getHabitaciones(): HabitacionFisica[] {
    return this.habitaciones;
  }

  /**
   * Busca una habitación física por ID.
   * @param id ID de la habitación.
   * @returns Habitación encontrada o undefined.
   */
  getHabitacionById(id: number): HabitacionFisica | undefined {
    return this.habitaciones.find((h) => h.id === id);
  }

  /**
   * Busca una habitación por su número interno.
   * @param numero Número de habitación.
   * @returns Habitación encontrada o undefined.
   */
  getHabitacionByNumero(numero: string): HabitacionFisica | undefined {
    return this.habitaciones.find((h) => h.numero === numero);
  }

  /**
   * Filtra habitaciones por tipo.
   * @param tipoId ID del tipo de habitación.
   * @returns Habitaciones que pertenecen al tipo indicado.
   */
  getHabitacionesByTipo(tipoId: number): HabitacionFisica[] {
    return this.habitaciones.filter((h) => h.tipoHabitacionId === tipoId);
  }

  /**
   * Lista las habitaciones con estado disponible.
   * @returns Habitaciones disponibles para reserva.
   */
  getHabitacionesDisponibles(): HabitacionFisica[] {
    return this.habitaciones.filter((h) => h.estado === 'DISPONIBLE');
  }

  /**
   * Filtra habitaciones por piso.
   * @param piso Número de piso.
   * @returns Habitaciones del piso solicitado.
   */
  getHabitacionesByPiso(piso: number): HabitacionFisica[] {
    return this.habitaciones.filter((h) => h.piso === piso);
  }

  crearHabitacion(habitacion: Omit<HabitacionFisica, 'id'>): HabitacionFisica {
    const nuevaHabitacion: HabitacionFisica = {
      ...habitacion,
      id: this.getNextId(),
    };
    this.habitaciones = [...this.habitaciones, nuevaHabitacion];
    return nuevaHabitacion;
  }

  actualizarHabitacion(
    id: number,
    cambios: Omit<HabitacionFisica, 'id'>,
  ): HabitacionFisica | undefined {
    const index = this.habitaciones.findIndex((h) => h.id === id);
    if (index === -1) {
      return undefined;
    }

    const actualizada: HabitacionFisica = { id, ...cambios };
    this.habitaciones[index] = actualizada;
    this.habitaciones = [...this.habitaciones];
    return actualizada;
  }

  eliminarHabitacion(id: number): boolean {
    const totalAntes = this.habitaciones.length;
    this.habitaciones = this.habitaciones.filter((h) => h.id !== id);
    return this.habitaciones.length < totalAntes;
  }

  private getNextId(): number {
    if (this.habitaciones.length === 0) {
      return 1;
    }
    return Math.max(...this.habitaciones.map((h) => h.id)) + 1;
  }
}
