import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { HabitacionFisica } from '../../models/habitacion-fisica';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { HabitacionFisicaService } from '../../services/habitacion-fisica.service';
import { TipoHabitacionService } from '../../services/tipo-habitacion.service';

@Component({
  selector: 'app-admin-habitaciones-lista',
  templateUrl: './admin-habitaciones-lista.component.html',
  styleUrls: ['./admin-habitaciones-lista.component.css'],
})
export class AdminHabitacionesListaComponent implements OnInit {
  habitaciones: HabitacionFisica[] = [];
  tiposHabitacion: TipoHabitacion[] = [];
  editandoId: number | null = null;
  errorGeneral = '';
  mensajeExito = '';

  form: Omit<HabitacionFisica, 'id'> = this.getEmptyForm();

  constructor(
    private habitacionFisicaService: HabitacionFisicaService,
    private tipoHabitacionService: TipoHabitacionService,
  ) {}

  ngOnInit(): void {
    void this.cargarHabitaciones();
    void this.cargarTiposHabitacion();
  }

  async cargarHabitaciones(): Promise<void> {
    try {
      this.habitaciones = await firstValueFrom(
        this.habitacionFisicaService.getHabitaciones$(),
      );
    } catch {
      this.habitaciones = [];
      this.errorGeneral =
        'No se pudieron cargar las habitaciones físicas desde el servidor.';
    }
  }

  async cargarTiposHabitacion(): Promise<void> {
    try {
      this.tiposHabitacion = await firstValueFrom(
        this.tipoHabitacionService.getAll$(),
      );
    } catch {
      this.tiposHabitacion = [];
    }
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

  async guardar(): Promise<void> {
    this.errorGeneral = '';
    this.mensajeExito = '';

    if (!this.form.numero.trim()) {
      this.errorGeneral = 'El número de habitación es obligatorio.';
      return;
    }

    if (!this.form.tipoHabitacionId || this.form.tipoHabitacionId <= 0) {
      this.errorGeneral = 'Debes seleccionar un tipo de habitación.';
      return;
    }

    try {
      if (this.editandoId !== null) {
        await firstValueFrom(
          this.habitacionFisicaService.actualizarHabitacion$(
            this.editandoId,
            this.form,
          ),
        );
        this.mensajeExito = 'Habitación actualizada correctamente.';
        this.cancelar();
        await this.cargarHabitaciones();
        return;
      }

      await firstValueFrom(
        this.habitacionFisicaService.crearHabitacion$(this.form),
      );
      this.mensajeExito = 'Habitación creada correctamente.';
      this.form = this.getEmptyForm();
      await this.cargarHabitaciones();
    } catch (error: any) {
      this.errorGeneral =
        error?.error?.error ||
        'No se pudo guardar la habitación. Verifica los datos e inténtalo de nuevo.';
    }
  }

  async eliminar(id: number): Promise<void> {
    this.errorGeneral = '';
    this.mensajeExito = '';

    try {
      await firstValueFrom(
        this.habitacionFisicaService.eliminarHabitacion$(id),
      );
      this.mensajeExito = 'Habitación eliminada correctamente.';
      await this.cargarHabitaciones();
    } catch (error: any) {
      this.errorGeneral =
        error?.error?.error ||
        'No se pudo eliminar la habitación. Puede estar asociada a reservas.';
    }
  }

  getNombreTipoHabitacion(tipoHabitacionId: number): string {
    const tipo = this.tiposHabitacion.find((t) => t.id === tipoHabitacionId);
    return tipo?.nombre || `Tipo #${tipoHabitacionId}`;
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
