export interface ReservaHabitacion {
  id: number;
  fechaCheckIn: string;
  fechaCheckOut: string;
  estado: 'PENDIENTE' | 'CONFIRMADA' | 'CHECKIN' | 'CHECKOUT' | 'CANCELADA';
  numeroHuespedes: number;
  clienteId: number;
  habitacionId: number;
  operarioId?: number;
}

export class ReservaHabitacionModel implements ReservaHabitacion {
  id: number = 0;
  fechaCheckIn: string = '';
  fechaCheckOut: string = '';
  estado: 'PENDIENTE' | 'CONFIRMADA' | 'CHECKIN' | 'CHECKOUT' | 'CANCELADA' =
    'PENDIENTE';
  numeroHuespedes: number = 0;
  clienteId: number = 0;
  habitacionId: number = 0;
  operarioId?: number;

  /**
   * Inicializa una reserva de habitación a partir de datos parciales.
   * @param data Datos opcionales de la reserva.
   */
  constructor(data?: Partial<ReservaHabitacion>) {
    Object.assign(this, data);
  }
}
