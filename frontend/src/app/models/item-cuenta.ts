export interface ItemCuenta {
  id: number;
  cuentaId: number;
  servicioId: number;
  fechaConsumo: string;
  servicioNombre?: string;
  servicioPrecio?: number;
}

export class ItemCuentaModel implements ItemCuenta {
  id: number = 0;
  cuentaId: number = 0;
  servicioId: number = 0;
  fechaConsumo: string = '';
  servicioNombre?: string;
  servicioPrecio?: number;

  /**
   * Inicializa un ítem de cuenta con datos parciales.
   * @param data Datos opcionales del ítem.
   */
  constructor(data?: Partial<ItemCuenta>) {
    Object.assign(this, data);
  }
}
