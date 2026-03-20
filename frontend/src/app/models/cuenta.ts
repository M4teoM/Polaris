import { ItemCuenta } from './item-cuenta';

export interface Cuenta {
  id: number;
  reservaId: number;
  pagada: boolean;
  items?: ItemCuenta[];
  total?: number;
}
