import { Injectable } from '@angular/core';
import { Cuenta } from '../models/cuenta';
import { ItemCuenta } from '../models/item-cuenta';

@Injectable({
  providedIn: 'root'
})
export class CuentaService {

  private cuentas: Cuenta[] = [
    {
      id: 1,
      reservaId: 1,
      pagada: false,
      items: [
        { id: 1, cuentaId: 1, servicioId: 1, fechaConsumo: '2026-03-21', servicioNombre: 'Spa Premium', servicioPrecio: 180000 },
        { id: 2, cuentaId: 1, servicioId: 3, fechaConsumo: '2026-03-22', servicioNombre: 'Restaurante Gourmet', servicioPrecio: 95000 }
      ],
      total: 275000
    },
    {
      id: 2,
      reservaId: 3,
      pagada: false,
      items: [
        { id: 3, cuentaId: 2, servicioId: 5, fechaConsumo: '2026-03-20', servicioNombre: 'Gimnasio', servicioPrecio: 25000 }
      ],
      total: 25000
    },
    {
      id: 3,
      reservaId: 5,
      pagada: true,
      items: [
        { id: 4, cuentaId: 3, servicioId: 2, fechaConsumo: '2026-03-16', servicioNombre: 'Limusina', servicioPrecio: 350000 },
        { id: 5, cuentaId: 3, servicioId: 4, fechaConsumo: '2026-03-17', servicioNombre: 'Piscina Infinity', servicioPrecio: 45000 }
      ],
      total: 395000
    }
  ];

  getCuentas(): Cuenta[] {
    return this.cuentas;
  }

  getCuentaById(id: number): Cuenta | undefined {
    return this.cuentas.find(c => c.id === id);
  }

  getCuentaByReserva(reservaId: number): Cuenta | undefined {
    return this.cuentas.find(c => c.reservaId === reservaId);
  }

  getCuentasPendientes(): Cuenta[] {
    return this.cuentas.filter(c => !c.pagada);
  }

  getCuentasPagadas(): Cuenta[] {
    return this.cuentas.filter(c => c.pagada);
  }

  calcularTotal(cuenta: Cuenta): number {
    if (!cuenta.items) return 0;
    return cuenta.items.reduce((sum, item) => sum + (item.servicioPrecio || 0), 0);
  }
}
