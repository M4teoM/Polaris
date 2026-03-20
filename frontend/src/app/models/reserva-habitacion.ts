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
