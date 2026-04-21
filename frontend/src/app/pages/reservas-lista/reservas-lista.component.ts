import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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

  constructor(
    private readonly reservaService: ReservaService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
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
    this.router.navigate(['/admin/reservas', id]);
  }

  editarReserva(id: number): void {
    this.router.navigate(['/admin/reservas/editar', id]);
  }

  async eliminarReserva(reserva: ReservaHabitacion): Promise<void> {
    const nombreCliente = this.getClienteNombre(reserva);
    const confirmado = window.confirm(
      `¿Deseas eliminar la reserva #${reserva.id} de ${nombreCliente}? Esta acción no se puede deshacer.`,
    );
    if (!confirmado) return;

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

  getClienteNombre(reserva: ReservaHabitacion): string {
    if (reserva.cliente) {
      return `${reserva.cliente.nombre || ''} ${reserva.cliente.apellido || ''}`.trim();
    }
    return `Cliente #${reserva.clienteId ?? '-'}`;
  }

  getHabitacionLabel(reserva: ReservaHabitacion): string {
    if (reserva.habitacion?.numero) {
      return `Hab. ${reserva.habitacion.numero}`;
    }
    return `Hab. #${reserva.habitacionId ?? '-'}`;
  }

  getTipoLabel(reserva: ReservaHabitacion): string {
    return reserva.habitacion?.tipoHabitacion?.nombre || '-';
  }
}
