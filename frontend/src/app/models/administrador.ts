export interface Administrador {
  id: number;
  correo: string;
  contrasena: string;
}

export class AdministradorModel implements Administrador {
  id: number = 0;
  correo: string = '';
  contrasena: string = '';

  constructor(data?: Partial<Administrador>) {
    Object.assign(this, data);
  }
}