import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ReservaHabitacion } from '../../models/reserva-habitacion';
import { ReservaService } from '../../services/reserva.service';

@Component({
  selector: 'app-reservas-lista',
  templateUrl: './reservas-lista.component.html',
  styleUrls: ['./reservas-lista.component.css'],
})
export class ReservasListaComponent implements OnInit {
  reservas: ReservaHabitacion[] = [];
  cargando = false;
  error = '';
  mensajeExito = '';
  readOnly = false;
  isOperador = false;
  routePrefix = '/admin/reservas';

  constructor(
    private readonly reservaService: ReservaService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.readOnly = !!this.route.snapshot.data['readOnly'];
    this.routePrefix =
      (this.route.snapshot.data['routePrefix'] as string) || this.routePrefix;
    this.isOperador = this.routePrefix.startsWith('/operador');
    void this.cargarReservas();
  }

  async cargarReservas(): Promise<void> {
    this.cargando = true;
    this.error = '';
    try {
      this.reservas = await firstValueFrom(this.reservaService.getReservas$());
    } catch {
      this.error = 'No se pudieron cargar las reservas.';
    } finally {
      this.cargando = false;
    }
  }

  verDetalle(id: number): void {
    this.router.navigate([this.routePrefix, id], {
      queryParams: { returnUrl: this.routePrefix },
    });
  }

  nuevaReserva(): void {
    this.router.navigateByUrl(`${this.routePrefix}/nueva`);
  }

  editarReserva(id: number): void {
    this.router.navigateByUrl(`${this.routePrefix}/editar/${id}`);
  }

  async eliminarReserva(reserva: ReservaHabitacion): Promise<void> {
    if (!window.confirm(
      `¿Eliminar la reserva #${reserva.id} de ${this.getClienteNombre(reserva)}? Esta acción no se puede deshacer.`
    )) return;
    this.error = '';
    try {
      await firstValueFrom(this.reservaService.delete$(reserva.id));
      this.mensajeExito = `Reserva #${reserva.id} eliminada correctamente.`;
      await this.cargarReservas();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch {
      this.error = 'No se pudo eliminar la reserva.';
    }
  }

  async activarEstadia(reserva: ReservaHabitacion): Promise<void> {
    if (!window.confirm(`¿Activar la estadía de la reserva #${reserva.id}?`)) return;
    this.error = '';
    try {
      await firstValueFrom(this.reservaService.activarEstadia$(reserva.id));
      this.mensajeExito = 'Estadía activada correctamente.';
      await this.cargarReservas();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.error = err?.error?.error || 'No se pudo activar la estadía.';
    }
  }

  async acabarEstadia(reserva: ReservaHabitacion): Promise<void> {
    if (!window.confirm(
      `¿Finalizar la estadía de la reserva #${reserva.id}? Solo es posible si la cuenta está pagada.`
    )) return;
    this.error = '';
    try {
      await firstValueFrom(this.reservaService.acabarEstadia$(reserva.id));
      this.mensajeExito = 'Estadía finalizada correctamente.';
      await this.cargarReservas();
      setTimeout(() => (this.mensajeExito = ''), 3000);
    } catch (err: any) {
      this.error = err?.error?.error || 'No se pudo finalizar la estadía.';
    }
  }

  getClienteNombre(reserva: ReservaHabitacion): string {
    if (reserva.clienteNombre) {
      return `${reserva.clienteNombre} ${reserva.clienteApellido || ''}`.trim();
    }
    return `Cliente #${reserva.clienteId ?? '-'}`;
  }

  getHabitacionLabel(reserva: ReservaHabitacion): string {
    if (reserva.habitacionNumero) return `Hab. ${reserva.habitacionNumero}`;
    return `Hab. #${reserva.habitacionId ?? '-'}`;
  }

  getTipoLabel(reserva: ReservaHabitacion): string {
    return reserva.tipoHabitacionNombre || '-';
  }
}
