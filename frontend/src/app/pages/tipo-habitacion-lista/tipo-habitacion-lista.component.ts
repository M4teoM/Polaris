import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { TipoHabitacionService } from '../../services/tipo-habitacion.service';

@Component({
  selector: 'app-tipo-habitacion-lista',
  templateUrl: './tipo-habitacion-lista.component.html',
  styleUrls: ['./tipo-habitacion-lista.component.css']
})
export class TipoHabitacionListaComponent implements OnInit {
  tiposHabitacion: TipoHabitacion[] = [];
  tipoAEliminar: TipoHabitacion | null = null;
  mostrarConfirmacion = false;
  mensajeExito = '';

  constructor(
    private tipoHabitacionService: TipoHabitacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarTipos();
  }

  cargarTipos(): void {
    this.tiposHabitacion = this.tipoHabitacionService.getAll();
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
  }

  cancelarEliminar(): void {
    this.tipoAEliminar = null;
    this.mostrarConfirmacion = false;
  }

  ejecutarEliminar(): void {
    if (this.tipoAEliminar) {
      this.tipoHabitacionService.delete(this.tipoAEliminar.id);
      this.mensajeExito = `"${this.tipoAEliminar.nombre}" eliminado correctamente.`;
      this.tipoAEliminar = null;
      this.mostrarConfirmacion = false;
      this.cargarTipos();
      setTimeout(() => this.mensajeExito = '', 3000);
    }
  }
}
