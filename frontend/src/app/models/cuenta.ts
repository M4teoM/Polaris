import { ItemCuenta } from './item-cuenta';

export interface Cuenta {
  id: number;
  reservaId: number;
  clienteId: number;
  pagada: boolean;
  items?: ItemCuenta[];
  total?: number;
}

export class CuentaModel implements Cuenta {
  id: number = 0;
  reservaId: number = 0;
  clienteId: number = 0;
  pagada: boolean = false;
  items?: ItemCuenta[];
  total?: number;

  constructor(data?: Partial<Cuenta>) {
    Object.assign(this, data);
  }
}