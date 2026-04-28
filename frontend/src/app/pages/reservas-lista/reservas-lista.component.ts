import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ReservaHabitacion } from '../../models/reserva-habitacion';
import { ReservaService } from '../../services/reserva.service';

/**
 * Componente que muestra la lista de reservas.
 * Es reutilizado por el panel de admin y el panel de operador
 * a través de los datos de ruta (readOnly, routePrefix).
 */
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
  routePrefix = '/admin/reservas';

  constructor(
    private readonly reservaService: ReservaService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  /** Inicializa el componente: determina modo y carga reservas. */
  ngOnInit(): void {
    this.readOnly = !!this.route.snapshot.data['readOnly'];
    this.routePrefix =
      (this.route.snapshot.data['routePrefix'] as string) || this.routePrefix;
    void this.cargarReservas();
  }

  /** Carga todas las reservas desde el backend. */
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

  /** Navega al detalle de una reserva. */
  verDetalle(id: number): void {
    this.router.navigate([this.routePrefix, id], {
      queryParams: { returnUrl: this.routePrefix },
    });
  }

  /** Navega al formulario de nueva reserva. */
  nuevaReserva(): void {
    this.router.navigateByUrl(`${this.routePrefix}/nueva`);
  }

  /** Navega al formulario de edición de una reserva. */
  editarReserva(id: number): void {
    this.router.navigateByUrl(`${this.routePrefix}/editar/${id}`);
  }

  /** Confirma y elimina una reserva. */
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

  /** Retorna el nombre completo del cliente usando los campos planos del DTO. */
  getClienteNombre(reserva: ReservaHabitacion): string {
    if (reserva.clienteNombre) {
      return `${reserva.clienteNombre} ${reserva.clienteApellido || ''}`.trim();
    }
    return `Cliente #${reserva.clienteId ?? '-'}`;
  }

  /** Retorna la etiqueta de habitación usando los campos planos del DTO. */
  getHabitacionLabel(reserva: ReservaHabitacion): string {
    if (reserva.habitacionNumero) return `Hab. ${reserva.habitacionNumero}`;
    return `Hab. #${reserva.habitacionId ?? '-'}`;
  }

  /** Retorna el nombre del tipo de habitación usando los campos planos del DTO. */
  getTipoLabel(reserva: ReservaHabitacion): string {
    return reserva.tipoHabitacionNombre || '-';
  }
}