import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReservaDTO } from '../../models/reserva-portal';
import { ReservaPortalService } from '../../services/reserva-portal.service';
/**
 * Componente que muestra las reservas activas del cliente.
 *
 * Permite listar, cancelar y editar reservas que aún están vigentes.
 * Una reserva se considera activa si no está cancelada y tiene
 * una fecha de salida vigente (hoy o en el futuro).
 */
@Component({
  selector: 'app-active-reservations',
  templateUrl: './active-reservations.component.html',
  styleUrls: ['./active-reservations.component.css'],
})
export class ActiveReservationsComponent implements OnInit {
  /**
   * Lista de reservas activas
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
   * Controla si se muestra el formulario de edición
   */
  mostrarFormulario = false;

  /**
   * Reserva actualmente en edición
   */
  reservaEnEdicion: ReservaDTO | null = null;

  /**
   * Datos del formulario de edición
   */
  formData = {
    fechaCheckIn: '',
    fechaCheckOut: '',
    numeroHuespedes: 0,
  };

  /**
   * Mensaje de éxito después de una operación
   */
  mensajeExito: string | null = null;

  constructor(
    private reservaPortalService: ReservaPortalService,
    private route: ActivatedRoute,
  ) {}

  /**
   * Inicializa el componente cargando las reservas activas
   */
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.clienteId = params['clienteId'];
      this.cargarReservasActivas();
    });
  }

  /**
   * Carga las reservas activas desde el backend
   */
  private cargarReservasActivas(): void {
    this.cargando = true;
    this.error = null;

    this.reservaPortalService.obtenerReservasActivas(this.clienteId).subscribe({
      next: (datos) => {
        this.reservas = datos;
        this.cargando = false;
      },
      error: (err) => {
        this.error =
          'No se pudieron cargar las reservas. Por favor, intenta más tarde.';
        console.error('Error al cargar reservas:', err);
        this.cargando = false;
      },
    });
  }

  /**
   * Abre el formulario para editar una reserva
   *
   * @param reserva reserva a editar
   */
  abrirEdicion(reserva: ReservaDTO): void {
    this.reservaEnEdicion = reserva;
    this.formData = {
      fechaCheckIn: reserva.fechaCheckIn,
      fechaCheckOut: reserva.fechaCheckOut,
      numeroHuespedes: reserva.numeroHuespedes,
    };
    this.mostrarFormulario = true;
    this.error = null;
  }

  /**
   * Cierra el formulario de edición
   */
  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.reservaEnEdicion = null;
  }

  /**
   * Envía la edición de la reserva al backend
   */
  guardarEdicion(): void {
    if (!this.reservaEnEdicion) return;

    this.reservaPortalService
      .actualizarReserva(
        this.clienteId,
        this.reservaEnEdicion.id,
        this.formData,
      )
      .subscribe({
        next: (reservaActualizada) => {
          // Actualiza la reserva en la lista
          const index = this.reservas.findIndex(
            (r) => r.id === reservaActualizada.id,
          );
          if (index > -1) {
            this.reservas[index] = reservaActualizada;
          }
          this.mensajeExito = 'Reserva actualizada exitosamente';
          this.cerrarFormulario();
          setTimeout(() => {
            this.mensajeExito = null;
          }, 3000);
        },
        error: (err) => {
          this.error = err.error?.error || 'Error al actualizar la reserva';
          console.error('Error:', err);
        },
      });
  }

  /**
   * Cancela una reserva
   *
   * @param reservaId ID de la reserva a cancelar
   */
  cancelarReserva(reservaId: number): void {
    if (!confirm('¿Estás seguro de que deseas cancelar esta reserva?')) {
      return;
    }

    this.reservaPortalService
      .cancelarReserva(this.clienteId, reservaId)
      .subscribe({
        next: (reservaCancelada) => {
          // Remueve la reserva cancelada de la lista
          this.reservas = this.reservas.filter((r) => r.id !== reservaId);
          this.mensajeExito = 'Reserva cancelada exitosamente';
          setTimeout(() => {
            this.mensajeExito = null;
          }, 3000);
        },
        error: (err) => {
          this.error = err.error?.error || 'Error al cancelar la reserva';
          console.error('Error:', err);
        },
      });
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
}
