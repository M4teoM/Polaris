import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  mensajeExito = '';
  errorGeneral = '';

  constructor(
    private servicioService: ServicioService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.cargarServicios();
  }

  cargarServicios(): void {
    this.servicioService.getServicios().subscribe((data) => {
      this.servicios = data;
    });
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
  }

  cancelarEliminar(): void {
    this.servicioAEliminar = null;
    this.mostrarConfirmacion = false;
  }

  ejecutarEliminar(): void {
    if (!this.servicioAEliminar) return;
    this.errorGeneral = '';
    this.servicioService.delete(this.servicioAEliminar.id).subscribe({
      next: () => {
        this.mensajeExito = `"${this.servicioAEliminar!.nombre}" eliminado correctamente.`;
        this.servicioAEliminar = null;
        this.mostrarConfirmacion = false;
        this.cargarServicios();
        setTimeout(() => (this.mensajeExito = ''), 3000);
      },
      error: (err) => {
        this.errorGeneral =
          err?.error?.error ||
          'No se pudo eliminar el servicio. Verifica si tiene datos asociados.';
        this.mostrarConfirmacion = false;
      },
    });
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0,
    }).format(price || 0);
  }
}
