export interface TipoHabitacion {
  id: number;
  nombre: string;
  descripcion: string;
  imagenUrl: string;
  precioPorNoche: number;
  metrosCuadrados: number;
  capacidad: number;
  tipoCama: string;
}

export class TipoHabitacionModel implements TipoHabitacion {
  id: number = 0;
  nombre: string = '';
  descripcion: string = '';
  imagenUrl: string = '';
  precioPorNoche: number = 0;
  metrosCuadrados: number = 0;
  capacidad: number = 0;
  tipoCama: string = '';

  /**
   * Inicializa un tipo de habitación con valores parciales.
   * @param data Datos opcionales del tipo de habitación.
   */
  constructor(data?: Partial<TipoHabitacion>) {
    Object.assign(this, data);
  }
}
