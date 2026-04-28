/**
 * Interfaz que representa una reserva recibida desde el backend.
 * Usa campos planos en lugar de objetos anidados.
 */
export interface ReservaHabitacion {
  id: number;
  fechaCheckIn: string;
  fechaCheckOut: string;
  estado: string;
  numeroHuespedes: number;
  // Campos planos del cliente
  clienteId?: number;
  clienteNombre?: string;
  clienteApellido?: string;
  clienteCorreo?: string;
  // Campos planos de la habitación
  habitacionId?: number;
  habitacionNumero?: string;
  tipoHabitacionId?: number;
  tipoHabitacionNombre?: string;
}

/**
 * Clase modelo con valores por defecto para crear una nueva reserva.
 */
export class ReservaHabitacionModel implements ReservaHabitacion {
  id: number = 0;
  fechaCheckIn: string = '';
  fechaCheckOut: string = '';
  estado: string = 'Inactiva';
  numeroHuespedes: number = 1;
  clienteId?: number;
  clienteNombre?: string;
  clienteApellido?: string;
  clienteCorreo?: string;
  habitacionId?: number;
  habitacionNumero?: string;
  tipoHabitacionId?: number;
  tipoHabitacionNombre?: string;

  /**
   * Inicializa una reserva a partir de datos parciales.
   * @param data Datos opcionales de la reserva.
   */
  constructor(data?: Partial<ReservaHabitacion>) {
    Object.assign(this, data);
  }
}