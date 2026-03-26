export interface Operario {
  id: number;
  correo: string;
  contrasena: string;
  nombre: string;
  administradorId: number;
}

export class OperarioModel implements Operario {
  id: number = 0;
  correo: string = '';
  contrasena: string = '';
  nombre: string = '';
  administradorId: number = 0;

  constructor(data?: Partial<Operario>) {
    Object.assign(this, data);
  }
}