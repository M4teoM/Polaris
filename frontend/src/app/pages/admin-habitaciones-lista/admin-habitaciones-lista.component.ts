import { Component, OnInit } from '@angular/core';
import { HabitacionFisica } from '../../models/habitacion-fisica';
import { HabitacionFisicaService } from '../../services/habitacion-fisica.service';

@Component({
  selector: 'app-admin-habitaciones-lista',
  templateUrl: './admin-habitaciones-lista.component.html',
  styleUrls: ['./admin-habitaciones-lista.component.css'],
})
export class AdminHabitacionesListaComponent implements OnInit {
  habitaciones: HabitacionFisica[] = [];
  editandoId: number | null = null;
  errorGeneral = '';
  mensajeExito = '';

  form: Omit<HabitacionFisica, 'id'> = this.getEmptyForm();

  constructor(private habitacionFisicaService: HabitacionFisicaService) {}

  ngOnInit(): void {
    this.cargarHabitaciones();
  }

  cargarHabitaciones(): void {
    this.habitaciones = this.habitacionFisicaService.getHabitaciones();
  }

  editar(habitacion: HabitacionFisica): void {
    this.editandoId = habitacion.id;
    this.form = {
      numero: habitacion.numero,
      piso: habitacion.piso,
      estado: habitacion.estado,
      tipoHabitacionId: habitacion.tipoHabitacionId,
      tipoHabitacion: habitacion.tipoHabitacion,
    };
  }

  guardar(): void {
    this.errorGeneral = '';

    if (!this.form.numero.trim()) {
      this.errorGeneral = 'El número de habitación es obligatorio.';
      return;
    }

    if (this.editandoId !== null) {
      const updated = this.habitacionFisicaService.actualizarHabitacion(
        this.editandoId,
        this.form,
      );
      if (!updated) {
        this.errorGeneral = 'No se encontró la habitación para actualizar.';
        return;
      }
      this.mensajeExito = 'Habitación actualizada correctamente.';
      this.cancelar();
      this.cargarHabitaciones();
      return;
    }

    this.habitacionFisicaService.crearHabitacion(this.form);
    this.mensajeExito = 'Habitación creada correctamente.';
    this.form = this.getEmptyForm();
    this.cargarHabitaciones();
  }

  eliminar(id: number): void {
    const ok = this.habitacionFisicaService.eliminarHabitacion(id);
    if (!ok) {
      this.errorGeneral = 'No se pudo eliminar la habitación.';
      return;
    }
    this.mensajeExito = 'Habitación eliminada correctamente.';
    this.cargarHabitaciones();
  }

  cancelar(): void {
    this.editandoId = null;
    this.form = this.getEmptyForm();
  }

  private getEmptyForm(): Omit<HabitacionFisica, 'id'> {
    return {
      numero: '',
      piso: 1,
      estado: 'DISPONIBLE',
      tipoHabitacionId: 1,
      tipoHabitacion: undefined,
    };
  }
}
