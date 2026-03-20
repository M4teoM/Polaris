import { TipoHabitacion } from './tipo-habitacion';

export interface HabitacionFisica {
  id: number;
  numero: string;
  piso: number;
  estado: 'DISPONIBLE' | 'OCUPADA' | 'MANTENIMIENTO' | 'LIMPIEZA';
  tipoHabitacionId: number;
  tipoHabitacion?: TipoHabitacion;
}
