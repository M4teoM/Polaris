// src/app/models/cuenta.ts — agregar campos del DTO del backend

import { ItemCuenta } from './item-cuenta';

export interface Cuenta {
  id: number;
  reservaId: number;
  clienteId: number;
  pagada: boolean;
  items?: ItemCuenta[];
  total?: number;
  // Campos de display del backend
  reservaHabitacionNumero?: string;
  clienteNombre?: string;
  clienteApellido?: string;
}

export class CuentaModel implements Cuenta {
  id: number = 0;
  reservaId: number = 0;
  clienteId: number = 0;
  pagada: boolean = false;
  items?: ItemCuenta[];
  total?: number;
  reservaHabitacionNumero?: string;
  clienteNombre?: string;
  clienteApellido?: string;

  constructor(data?: Partial<Cuenta>) {
    Object.assign(this, data);
  }
}
