import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Servicio } from '../../models/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-admin-servicios-lista',
  templateUrl: './admin-servicios-lista.component.html',
  styleUrls: ['./admin-servicios-lista.component.css'],
})
export class AdminServiciosListaComponent implements OnInit {
  servicios: Servicio[] = [];
  servicioAEliminar: Servicio | null = null;
  mostrarConfirmacion = false;
  mostrarConfirmacionForzada = false;
  motivoBloqueoEliminacion = '';
  mensajeExito = '';
  errorGeneral = '';

  constructor(
    private servicioService: ServicioService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    void this.cargarServicios();
  }

  async cargarServicios(): Promise<void> {
    this.errorGeneral = '';

    try {
      this.servicios = await firstValueFrom(
        this.servicioService.getServicios$(),
      );
    } catch (_error) {
      this.errorGeneral = 'No se pudieron cargar los servicios.';
    }
  }

  irACrear(): void {
    this.router.navigate(['/admin/servicios/nuevo']);
  }
  irAEditar(id: number): void {
    this.router.navigate(['/admin/servicios/editar', id]);
  }

  confirmarEliminar(servicio: Servicio): void {
    this.servicioAEliminar = servicio;
    this.mostrarConfirmacion = true;
    this.mostrarConfirmacionForzada = false;
    this.motivoBloqueoEliminacion = '';
  }

  cancelarEliminar(): void {
    this.servicioAEliminar = null;
    this.mostrarConfirmacion = false;
    this.mostrarConfirmacionForzada = false;
    this.motivoBloqueoEliminacion = '';
  }

  async ejecutarEliminar(): Promise<void> {
    if (!this.servicioAEliminar) return;

    this.errorGeneral = '';
    const servicioActual = this.servicioAEliminar;

    try {
      this.servicios = await firstValueFrom(
        this.servicioService
          .delete$(servicioActual.id)
          .pipe(switchMap(() => this.servicioService.getServicios$())),
      );
      this.mensajeExito = `"${servicioActual.nombre}" eliminado correctamente.`;
      this.servicioAEliminar = null;
      this.mostrarConfirmacion = false;
      this.mostrarConfirmacionForzada = false;
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.motivoBloqueoEliminacion =
        err?.error?.error ||
        'No se pudo eliminar el servicio. Verifica si tiene datos asociados.';
      this.mostrarConfirmacion = false;
      this.mostrarConfirmacionForzada = true;
    }
  }

  async ejecutarEliminarForzado(): Promise<void> {
    if (!this.servicioAEliminar) return;

    this.errorGeneral = '';
    const servicioActual = this.servicioAEliminar;

    try {
      this.servicios = await firstValueFrom(
        this.servicioService
          .delete$(servicioActual.id, true)
          .pipe(switchMap(() => this.servicioService.getServicios$())),
      );
      this.mensajeExito = `"${servicioActual.nombre}" eliminado de todos modos.`;
      this.servicioAEliminar = null;
      this.mostrarConfirmacion = false;
      this.mostrarConfirmacionForzada = false;
      this.motivoBloqueoEliminacion = '';
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error ||
        'No se pudo eliminar el servicio de forma forzada.';
      this.mostrarConfirmacionForzada = false;
    }
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0,
    }).format(price || 0);
  }
}
