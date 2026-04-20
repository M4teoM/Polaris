export interface ReservaHabitacion {
  id: number;
  fechaCheckIn: string;
  fechaCheckOut: string;
  estado: string;
  numeroHuespedes: number;
  clienteId?: number;
  habitacionId?: number;
  cliente?: {
    id?: number;
    nombre?: string;
    apellido?: string;
    correo?: string;
  };
  habitacion?: {
    id?: number;
    numero?: string;
    estado?: string;
    tipoHabitacion?: {
      id?: number;
      nombre?: string;
      capacidad?: number;
    };
  };
  operarioId?: number;
}

export class ReservaHabitacionModel implements ReservaHabitacion {
  id: number = 0;
  fechaCheckIn: string = '';
  fechaCheckOut: string = '';
  estado: string = 'Pendiente';
  numeroHuespedes: number = 0;
  clienteId?: number;
  habitacionId?: number;
  cliente?: {
    id?: number;
    nombre?: string;
    apellido?: string;
    correo?: string;
  };
  habitacion?: {
    id?: number;
    numero?: string;
    estado?: string;
    tipoHabitacion?: {
      id?: number;
      nombre?: string;
      capacidad?: number;
    };
  };
  operarioId?: number;

  /**
   * Inicializa una reserva de habitación a partir de datos parciales.
   * @param data Datos opcionales de la reserva.
   */
  constructor(data?: Partial<ReservaHabitacion>) {
    Object.assign(this, data);
  }
}
