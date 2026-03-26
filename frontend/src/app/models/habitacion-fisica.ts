import { TipoHabitacion } from './tipo-habitacion';

export interface HabitacionFisica {
  id: number;
  numero: string;
  piso: number;
  estado: 'DISPONIBLE' | 'OCUPADA' | 'MANTENIMIENTO' | 'LIMPIEZA';
  tipoHabitacionId: number;
  tipoHabitacion?: TipoHabitacion;
}

export class HabitacionFisicaModel implements HabitacionFisica {
  id: number = 0;
  numero: string = '';
  piso: number = 0;
  estado: 'DISPONIBLE' | 'OCUPADA' | 'MANTENIMIENTO' | 'LIMPIEZA' = 'DISPONIBLE';
  tipoHabitacionId: number = 0;
  tipoHabitacion?: TipoHabitacion;

  constructor(data?: Partial<HabitacionFisica>) {
    Object.assign(this, data);
  }
}