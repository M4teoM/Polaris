import { Injectable } from '@angular/core';
import { ReservaHabitacion } from '../models/reserva-habitacion';

@Injectable({
  providedIn: 'root',
})
export class ReservaService {
  private reservas: ReservaHabitacion[] = [
    {
      id: 1,
      fechaCheckIn: '2026-03-20',
      fechaCheckOut: '2026-03-25',
      estado: 'CHECKIN',
      numeroHuespedes: 2,
      clienteId: 1,
      habitacionId: 2,
      operarioId: 1,
    },
    {
      id: 2,
      fechaCheckIn: '2026-03-22',
      fechaCheckOut: '2026-03-24',
      estado: 'CONFIRMADA',
      numeroHuespedes: 2,
      clienteId: 2,
      habitacionId: 7,
      operarioId: 2,
    },
    {
      id: 3,
      fechaCheckIn: '2026-03-19',
      fechaCheckOut: '2026-03-21',
      estado: 'CHECKIN',
      numeroHuespedes: 1,
      clienteId: 3,
      habitacionId: 12,
      operarioId: 1,
    },
    {
      id: 4,
      fechaCheckIn: '2026-03-25',
      fechaCheckOut: '2026-03-28',
      estado: 'PENDIENTE',
      numeroHuespedes: 3,
      clienteId: 4,
      habitacionId: 6,
    },
    {
      id: 5,
      fechaCheckIn: '2026-03-15',
      fechaCheckOut: '2026-03-18',
      estado: 'CHECKOUT',
      numeroHuespedes: 2,
      clienteId: 5,
      habitacionId: 10,
      operarioId: 2,
    },
  ];

  /**
   * Obtiene todas las reservas registradas.
   * @returns Arreglo de reservas.
   */
  getReservas(): ReservaHabitacion[] {
    return this.reservas;
  }

  /**
   * Busca una reserva por su ID.
   * @param id ID de la reserva.
   * @returns Reserva encontrada o undefined.
   */
  getReservaById(id: number): ReservaHabitacion | undefined {
    return this.reservas.find((r) => r.id === id);
  }

  /**
   * Filtra reservas pertenecientes a un cliente.
   * @param clienteId ID del cliente.
   * @returns Reservas del cliente indicado.
   */
  getReservasByCliente(clienteId: number): ReservaHabitacion[] {
    return this.reservas.filter((r) => r.clienteId === clienteId);
  }

  /**
   * Filtra reservas asociadas a una habitación específica.
   * @param habitacionId ID de la habitación.
   * @returns Reservas encontradas para la habitación.
   */
  getReservasByHabitacion(habitacionId: number): ReservaHabitacion[] {
    return this.reservas.filter((r) => r.habitacionId === habitacionId);
  }

  /**
   * Filtra reservas por estado operativo.
   * @param estado Estado de reserva a consultar.
   * @returns Reservas que coinciden con el estado.
   */
  getReservasByEstado(estado: string): ReservaHabitacion[] {
    return this.reservas.filter((r) => r.estado === estado);
  }

  /**
   * Obtiene reservas activas considerando estados aún no finalizados.
   * @returns Reservas en curso o pendientes.
   */
  getReservasActivas(): ReservaHabitacion[] {
    return this.reservas.filter(
      (r) =>
        r.estado === 'CONFIRMADA' ||
        r.estado === 'CHECKIN' ||
        r.estado === 'PENDIENTE',
    );
  }
}
