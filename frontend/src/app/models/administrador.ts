export interface Administrador {
  id: number;
  correo: string;
  contrasena: string;
}

export class AdministradorModel implements Administrador {
  id: number = 0;
  correo: string = '';
  contrasena: string = '';

  /**
   * Inicializa una instancia de administrador combinando valores por defecto y datos parciales.
   * @param data Datos opcionales para hidratar el modelo.
   */
  constructor(data?: Partial<Administrador>) {
    Object.assign(this, data);
  }
}
