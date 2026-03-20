export interface ItemCuenta {
  id: number;
  cuentaId: number;
  servicioId: number;
  fechaConsumo: string;
  servicioNombre?: string;
  servicioPrecio?: number;
}
