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
