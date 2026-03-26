export interface Cliente {
  id?: number;
  nombre: string;
  apellido: string;
  correo: string;
  contrasena?: string;
  cedula?: string;
  telefono?: string;
}

export class ClienteModel implements Cliente {
  id?: number;
  nombre: string = '';
  apellido: string = '';
  correo: string = '';
  contrasena?: string;
  cedula?: string;
  telefono?: string;

  /**
   * Inicializa una instancia de cliente a partir de datos parciales.
   * @param data Datos opcionales para poblar el modelo.
   */
  constructor(data?: Partial<Cliente>) {
    Object.assign(this, data);
  }
}
