import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  mensajeExito = '';

  constructor(
    private tipoHabitacionService: TipoHabitacionService,
    private router: Router,
  ) {}

  /**
   * Carga los tipos de habitación al iniciar la vista.
   */
  ngOnInit(): void {
    this.cargarTipos();
  }

  /**
   * Recupera y asigna el catálogo de tipos de habitación.
   */
  cargarTipos(): void {
    this.tiposHabitacion = this.tipoHabitacionService.getAll();
  }

  /**
   * Formatea precio para visualización en tabla.
   * @param price Precio a formatear.
   * @returns Texto de precio formateado.
   */
  formatPrice(price: number): string {
    return this.tipoHabitacionService.formatPrice(price);
  }

  /**
   * Navega al formulario de creación de tipo de habitación.
   */
  irACrear(): void {
    this.router.navigate(['/admin/tipos-habitacion/nuevo']);
  }

  /**
   * Navega al formulario de edición del tipo seleccionado.
   * @param id ID del tipo de habitación a editar.
   */
  irAEditar(id: number): void {
    this.router.navigate(['/admin/tipos-habitacion/editar', id]);
  }

  /**
   * Abre el modal/estado de confirmación para eliminar un tipo.
   * @param tipo Tipo de habitación candidato a eliminación.
   */
  confirmarEliminar(tipo: TipoHabitacion): void {
    this.tipoAEliminar = tipo;
    this.mostrarConfirmacion = true;
  }

  /**
   * Cancela el flujo de eliminación y limpia selección.
   */
  cancelarEliminar(): void {
    this.tipoAEliminar = null;
    this.mostrarConfirmacion = false;
  }

  /**
   * Ejecuta la eliminación confirmada y refresca el listado.
   */
  ejecutarEliminar(): void {
    if (this.tipoAEliminar) {
      this.tipoHabitacionService.delete(this.tipoAEliminar.id);
      this.mensajeExito = `"${this.tipoAEliminar.nombre}" eliminado correctamente.`;
      this.tipoAEliminar = null;
      this.mostrarConfirmacion = false;
      this.cargarTipos();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    }
  }
}
