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

  /**
   * Inicializa una habitación aplicando datos parciales sobre valores por defecto.
   * @param data Datos opcionales de la habitación.
   */
  constructor(data?: Partial<Habitacion>) { //el partial se encarga de que los datos recibidos no sean obligatorios
    Object.assign(this, data);
  }
}
