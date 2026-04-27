import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Servicio } from '../../models/servicio';
import { OperadorServicioService, InfoHabitacionServicio } from '../../services/operador-servicio.service';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-operador-contratar-servicio',
  templateUrl: './operador-contratar-servicio.component.html',
  styleUrls: ['./operador-contratar-servicio.component.css'],
})
export class OperadorContratarServicioComponent {
  numeroHabitacion = '';
  servicioId = 0;

  cargandoInfo = false;
  contratando = false;

  servicios: Servicio[] = [];
  infoHabitacion: InfoHabitacionServicio | null = null;
  error = '';
  ok = '';

  constructor(
    private readonly operadorServicioService: OperadorServicioService,
    private readonly servicioService: ServicioService,
  ) {
    void this.cargarServicios();
  }

  async cargarServicios(): Promise<void> {
    try {
      this.servicios = await firstValueFrom(this.servicioService.getServicios$());
    } catch {
      this.error = 'No se pudieron cargar los servicios.';
    }
  }

  async buscarHabitacion(): Promise<void> {
    this.error = '';
    this.ok = '';
    this.infoHabitacion = null;

    if (!this.numeroHabitacion.trim()) {
      this.error = 'Debes ingresar un número de habitación.';
      return;
    }

    this.cargandoInfo = true;
    try {
      this.infoHabitacion = await firstValueFrom(
        this.operadorServicioService.buscarInfoPorHabitacion$(this.numeroHabitacion.trim()),
      );
    } catch (err) {
      const e = err as HttpErrorResponse;
      this.error = e?.error?.error || 'No se encontró una reserva vigente para esa habitación.';
    } finally {
      this.cargandoInfo = false;
    }
  }

  async contratarServicio(): Promise<void> {
    this.error = '';
    this.ok = '';

    if (!this.infoHabitacion) {
      this.error = 'Primero debes buscar una habitación válida.';
      return;
    }

    if (!this.servicioId) {
      this.error = 'Selecciona un servicio.';
      return;
    }

    this.contratando = true;
    try {
      const res = await firstValueFrom(
        this.operadorServicioService.contratarServicio$(
          this.infoHabitacion.habitacionNumero,
          this.servicioId,
        ),
      );
      this.ok = `${res.mensaje} Total actual de cuenta: $${Math.round(res.totalCuenta).toLocaleString('es-CO')}`;
      this.infoHabitacion = {
        ...this.infoHabitacion,
        cuentaId: res.cuentaId,
        totalCuenta: res.totalCuenta,
      };
      this.servicioId = 0;
    } catch (err) {
      const e = err as HttpErrorResponse;
      this.error = e?.error?.error || 'No se pudo contratar el servicio.';
    } finally {
      this.contratando = false;
    }
  }
}
