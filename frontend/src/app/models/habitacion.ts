export interface Habitacion {
  id: number;
  nombre: string;
  descripcion: string;
  imagenUrl: string;
  precioPorNoche: number;
  metrosCuadrados: number;
  tipoCama: string;
  capacidad: number;
  disponible?: boolean;
}

export class HabitacionModel implements Habitacion {
  id: number = 0;
  nombre: string = '';
  descripcion: string = '';
  imagenUrl: string = '';
  precioPorNoche: number = 0;
  metrosCuadrados: number = 0;
  tipoCama: string = '';
  capacidad: number = 0;
  disponible?: boolean;

  constructor(data?: Partial<Habitacion>) {
    Object.assign(this, data);
  }
}