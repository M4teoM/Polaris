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

  constructor(data?: Partial<Cliente>) {
    Object.assign(this, data);
  }
}