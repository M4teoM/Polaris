import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReservaDTO } from '../../models/reserva-portal';
import { ReservaPortalService } from '../../services/reserva-portal.service';
/**
 * Componente que muestra el historial de reservas del cliente.
 *
 * Incluye todas las reservas finalizadas (por fecha) y canceladas,
 * mostrando badges de colores para cada estado.
 */
@Component({
  selector: 'app-reservation-history',
  templateUrl: './reservation-history.component.html',
  styleUrls: ['./reservation-history.component.css'],
})
export class ReservationHistoryComponent implements OnInit {
  /**
   * Lista de reservas del historial
   */
  reservas: ReservaDTO[] = [];

  /**
   * Indica si se están cargando las reservas
   */
  cargando = false;

  /**
   * Mensaje de error si algo falla
   */
  error: string | null = null;

  /**
   * ID del cliente obtenido de la ruta
   */
  /**
   * ID del cliente (puede venir por ruta o por input)
   */
  @Input() clienteId: number = 0;

  /**
   * Filtro de estado para mostrar solo reservas del tipo seleccionado
   */
  filtroEstado = 'todos'; // 'todos', 'completadas', 'canceladas'

  constructor(
    private reservaPortalService: ReservaPortalService,
    private route: ActivatedRoute,
  ) {}

  /**
   * Inicializa el componente cargando el historial de reservas
   */
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.clienteId = params['clienteId'];
      this.cargarHistorial();
    });
  }

  /**
   * Carga el historial de reservas desde el backend
   */
  private cargarHistorial(): void {
    this.cargando = true;
    this.error = null;

    this.reservaPortalService.obtenerHistorial(this.clienteId).subscribe({
      next: (datos) => {
        this.reservas = datos;
        this.cargando = false;
      },
      error: (err) => {
        this.error =
          'No se pudo cargar el historial. Por favor, intenta más tarde.';
        console.error('Error al cargar historial:', err);
        this.cargando = false;
      },
    });
  }

  /**
   * Retorna las reservas filtradas según el estado seleccionado
   */
  get reservasFiltradas(): ReservaDTO[] {
    if (this.filtroEstado === 'todos') {
      return this.reservas;
    }

    if (this.filtroEstado === 'canceladas') {
      return this.reservas.filter((r) =>
        r.estado.toLowerCase().includes('cancelado'),
      );
    }

    // 'completadas'
    return this.reservas.filter(
      (r) => !r.estado.toLowerCase().includes('cancelado'),
    );
  }

  /**
   * Retorna la clase CSS del badge según el estado de la reserva
   *
   * @param estado estado de la reserva
   * @returns clase CSS del badge
   */
  getBadgeClass(estado: string): string {
    const estadoLower = estado.toLowerCase();

    if (estadoLower.includes('cancelado')) {
      return 'badge-cancelled';
    }

    if (
      estadoLower.includes('finalizada') ||
      estadoLower.includes('inactiva')
    ) {
      return 'badge-completed';
    }

    return 'badge-inactive';
  }

  /**
   * Retorna el texto del badge según el estado de la reserva
   *
   * @param estado estado de la reserva
   * @returns texto del badge
   */
  getBadgeText(estado: string): string {
    const estadoLower = estado.toLowerCase();

    if (estadoLower.includes('cancelado')) {
      return 'Cancelada';
    }

    if (estadoLower.includes('finalizada')) {
      return 'Finalizada';
    }

    if (estadoLower.includes('inactiva')) {
      return 'Completada';
    }

    return estado;
  }

  /**
   * Retorna un ícono según el estado de la reserva
   *
   * @param estado estado de la reserva
   * @returns ícono
   */
  getEstadoIcon(estado: string): string {
    const estadoLower = estado.toLowerCase();

    if (estadoLower.includes('cancelado')) {
      return '❌';
    }

    if (
      estadoLower.includes('finalizada') ||
      estadoLower.includes('inactiva')
    ) {
      return '✓';
    }

    return '📋';
  }

  contarCompletadas(): number {
    return this.reservas.filter(
      (r) => !r.estado.toLowerCase().includes('cancelado'),
    ).length;
  }

  contarCanceladas(): number {
    return this.reservas.filter((r) =>
      r.estado.toLowerCase().includes('cancelado'),
    ).length;
  }

  /**
   * Calcula los días de duración de la reserva
   *
   * @param reserva reserva
   * @returns número de días
   */
  calcularDias(reserva: ReservaDTO): number {
    const inicio = new Date(reserva.fechaCheckIn);
    const fin = new Date(reserva.fechaCheckOut);
    return Math.ceil((fin.getTime() - inicio.getTime()) / (1000 * 3600 * 24));
  }

  /**
   * Formatea una fecha al formato español (dd/mm/yyyy)
   *
   * @param fecha fecha en formato ISO
   * @returns fecha formateada
   */
  formatearFecha(fecha: string): string {
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES');
  }

  /**
   * Cambia el filtro de estado
   *
   * @param estado nuevo estado de filtro
   */
  cambiarFiltro(estado: string): void {
    this.filtroEstado = estado;
  }
}
