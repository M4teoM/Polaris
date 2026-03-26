export interface Servicio {
  id: number;
  nombre: string;
  descripcion: string;
  descripcionDetallada?: string;
  precio: number;
  imagenUrl: string;
  categoria: string;
  duracion?: string;
  horario?: string;
  incluye?: string;
  destacados?: string;
  icono?: string;
}

export class ServicioModel implements Servicio {
  id: number = 0;
  nombre: string = '';
  descripcion: string = '';
  descripcionDetallada?: string;
  precio: number = 0;
  imagenUrl: string = '';
  categoria: string = '';
  duracion?: string;
  horario?: string;
  incluye?: string;
  destacados?: string;
  icono?: string;

  constructor(data?: Partial<Servicio>) {
    Object.assign(this, data);
  }
}