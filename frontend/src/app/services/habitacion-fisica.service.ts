import { Injectable } from '@angular/core';
import { HabitacionFisica } from '../models/habitacion-fisica';

@Injectable({
  providedIn: 'root'
})
export class HabitacionFisicaService {

  private habitaciones: HabitacionFisica[] = [
    // Suite Presidencial (tipo 1)
    { id: 1, numero: '501', piso: 5, estado: 'DISPONIBLE', tipoHabitacionId: 1 },
    { id: 2, numero: '502', piso: 5, estado: 'OCUPADA', tipoHabitacionId: 1 },
    
    // Suite Ejecutiva (tipo 2)
    { id: 3, numero: '401', piso: 4, estado: 'DISPONIBLE', tipoHabitacionId: 2 },
    { id: 4, numero: '402', piso: 4, estado: 'DISPONIBLE', tipoHabitacionId: 2 },
    { id: 5, numero: '403', piso: 4, estado: 'MANTENIMIENTO', tipoHabitacionId: 2 },
    
    // Habitación Deluxe (tipo 3)
    { id: 6, numero: '301', piso: 3, estado: 'DISPONIBLE', tipoHabitacionId: 3 },
    { id: 7, numero: '302', piso: 3, estado: 'OCUPADA', tipoHabitacionId: 3 },
    { id: 8, numero: '303', piso: 3, estado: 'DISPONIBLE', tipoHabitacionId: 3 },
    { id: 9, numero: '304', piso: 3, estado: 'LIMPIEZA', tipoHabitacionId: 3 },
    
    // Habitación Estándar (tipo 4)
    { id: 10, numero: '201', piso: 2, estado: 'DISPONIBLE', tipoHabitacionId: 4 },
    { id: 11, numero: '202', piso: 2, estado: 'DISPONIBLE', tipoHabitacionId: 4 },
    { id: 12, numero: '203', piso: 2, estado: 'OCUPADA', tipoHabitacionId: 4 },
    { id: 13, numero: '204', piso: 2, estado: 'DISPONIBLE', tipoHabitacionId: 4 },
    { id: 14, numero: '101', piso: 1, estado: 'DISPONIBLE', tipoHabitacionId: 4 },
    { id: 15, numero: '102', piso: 1, estado: 'DISPONIBLE', tipoHabitacionId: 4 }
  ];

  getHabitaciones(): HabitacionFisica[] {
    return this.habitaciones;
  }

  getHabitacionById(id: number): HabitacionFisica | undefined {
    return this.habitaciones.find(h => h.id === id);
  }

  getHabitacionByNumero(numero: string): HabitacionFisica | undefined {
    return this.habitaciones.find(h => h.numero === numero);
  }

  getHabitacionesByTipo(tipoId: number): HabitacionFisica[] {
    return this.habitaciones.filter(h => h.tipoHabitacionId === tipoId);
  }

  getHabitacionesDisponibles(): HabitacionFisica[] {
    return this.habitaciones.filter(h => h.estado === 'DISPONIBLE');
  }

  getHabitacionesByPiso(piso: number): HabitacionFisica[] {
    return this.habitaciones.filter(h => h.piso === piso);
  }
}
