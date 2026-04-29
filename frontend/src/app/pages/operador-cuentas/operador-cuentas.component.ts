import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Cuenta } from '../../models/cuenta';
import { CuentaService } from '../../services/cuenta.service';

@Component({
  selector: 'app-operador-cuentas',
  templateUrl: './operador-cuentas.component.html',
  styleUrls: ['./operador-cuentas.component.css'],
})
export class OperadorCuentasComponent implements OnInit {
  cuentas: Cuenta[] = [];
  cargando = false;
  error = '';
  mensajeExito = '';

  constructor(private readonly cuentaService: CuentaService) {}

  ngOnInit(): void {
    void this.cargarCuentas();
  }

  async cargarCuentas(): Promise<void> {
    this.cargando = true;
    this.error = '';
    try {
      this.cuentas = await firstValueFrom(this.cuentaService.getCuentas$());
    } catch {
      this.error = 'No se pudieron cargar las cuentas.';
    } finally {
      this.cargando = false;
    }
  }

  async pagarItem(cuentaId: number, itemId: number): Promise<void> {
    if (!window.confirm('¿Marcar este servicio como pagado?')) return;
    this.error = '';
    try {
      await firstValueFrom(this.cuentaService.pagarItem$(itemId));
      this.mensajeExito = 'Servicio pagado correctamente.';
      await this.cargarCuentas();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.error = err?.error?.error || 'No se pudo pagar el servicio.';
    }
  }

  async eliminarItem(cuentaId: number, itemId: number): Promise<void> {
    if (!window.confirm('¿Quitar este servicio de la cuenta?')) return;
    this.error = '';
    try {
      await firstValueFrom(this.cuentaService.eliminarItem$(cuentaId, itemId));
      this.mensajeExito = 'Servicio eliminado de la cuenta.';
      await this.cargarCuentas();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.error = err?.error?.error || 'No se pudo eliminar el servicio.';
    }
  }

  calcularTotal(cuenta: Cuenta): number {
    return (cuenta.items || []).reduce((sum, item) => sum + (item.servicioPrecio || 0), 0);
  }
}
