import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { TipoHabitacionService } from '../../services/tipo-habitacion.service';

@Component({
  selector: 'app-tipo-habitacion-lista',
  templateUrl: './tipo-habitacion-lista.component.html',
  styleUrls: ['./tipo-habitacion-lista.component.css'],
})
export class TipoHabitacionListaComponent implements OnInit {
  tiposHabitacion: TipoHabitacion[] = [];
  tipoAEliminar: TipoHabitacion | null = null;
  mostrarConfirmacion = false;
  mostrarConfirmacionForzada = false;
  motivoBloqueoEliminacion = '';
  mensajeExito = '';
  errorGeneral = '';

  constructor(
    private tipoHabitacionService: TipoHabitacionService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    void this.cargarTipos();
  }

  async cargarTipos(): Promise<void> {
    try {
      this.tiposHabitacion = await firstValueFrom(
        this.tipoHabitacionService.getAll(),
      );
    } catch {
      this.errorGeneral = 'No se pudieron cargar los tipos de habitación.';
    }
  }

  formatPrice(price: number): string {
    return this.tipoHabitacionService.formatPrice(price);
  }

  irACrear(): void {
    this.router.navigate(['/admin/tipos-habitacion/nuevo']);
  }

  irAEditar(id: number): void {
    this.router.navigate(['/admin/tipos-habitacion/editar', id]);
  }

  confirmarEliminar(tipo: TipoHabitacion): void {
    this.tipoAEliminar = tipo;
    this.mostrarConfirmacion = true;
    this.mostrarConfirmacionForzada = false;
    this.motivoBloqueoEliminacion = '';
  }

  cancelarEliminar(): void {
    this.tipoAEliminar = null;
    this.mostrarConfirmacion = false;
    this.mostrarConfirmacionForzada = false;
    this.motivoBloqueoEliminacion = '';
  }

  async ejecutarEliminar(): Promise<void> {
    if (!this.tipoAEliminar) {
      return;
    }

    this.errorGeneral = '';
    const tipoActual = this.tipoAEliminar;

    try {
      await firstValueFrom(this.tipoHabitacionService.delete(tipoActual.id!));
      this.mensajeExito = `"${tipoActual.nombre}" eliminado correctamente.`;
      this.tipoAEliminar = null;
      this.mostrarConfirmacion = false;
      await this.cargarTipos();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.motivoBloqueoEliminacion =
        err?.error?.error ||
        'No se pudo eliminar. Este tipo puede tener habitaciones asociadas.';
      this.mostrarConfirmacion = false;
      this.mostrarConfirmacionForzada = true;
    }
  }

  async ejecutarEliminarForzado(): Promise<void> {
    if (!this.tipoAEliminar) {
      return;
    }

    this.errorGeneral = '';
    const tipoActual = this.tipoAEliminar;

    try {
      await firstValueFrom(
        this.tipoHabitacionService.delete(tipoActual.id!, true),
      );
      this.mensajeExito = `"${tipoActual.nombre}" eliminado de todos modos.`;
      this.tipoAEliminar = null;
      this.mostrarConfirmacion = false;
      this.mostrarConfirmacionForzada = false;
      this.motivoBloqueoEliminacion = '';
      await this.cargarTipos();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error ||
        'No se pudo eliminar el tipo de habitación de forma forzada.';
      this.mostrarConfirmacion = false;
    }
  }
}
