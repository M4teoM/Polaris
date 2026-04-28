import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaDTO, ActualizarReservaDTO } from '../models/reserva-portal';

/**
 * Servicio Angular para gestionar reservas desde el Portal de Usuario.
 *
 * Proporciona métodos para listar reservas activas, historial,
 * cancelar reservas y actualizar fechas.
 * Todos los endpoints inician con /api/portal-usuario.
 */
@Injectable({
  providedIn: 'root',
})
export class ReservaPortalService {
  private readonly apiUrl = 'http://localhost:8080/api/portal-usuario';

  constructor(private http: HttpClient) {}

  /**
   * Obtiene las reservas activas del cliente.
   *
   * Reservas activas son aquellas que no están canceladas y
   * tienen una fecha de salida vigente (hoy o en el futuro).
   *
   * @param clienteId identificador del cliente
   * @returns Observable con lista de reservas activas
   */
  obtenerReservasActivas(clienteId: number): Observable<ReservaDTO[]> {
    return this.http.get<ReservaDTO[]>(
      `${this.apiUrl}/clientes/${clienteId}/reservas/activas`,
    );
  }

  /**
   * Obtiene el historial de reservas del cliente.
   *
   * Incluye reservas finalizadas (por fecha) y canceladas explícitamente.
   *
   * @param clienteId identificador del cliente
   * @returns Observable con lista de reservas históricas
   */
  obtenerHistorial(clienteId: number): Observable<ReservaDTO[]> {
    return this.http.get<ReservaDTO[]>(
      `${this.apiUrl}/clientes/${clienteId}/reservas/historial`,
    );
  }

  /**
   * Cancela una reserva del cliente.
   *
   * Cambia el estado de la reserva a CANCELADO.
   * Solo el cliente dueño de la reserva puede cancelarla.
   *
   * @param clienteId identificador del cliente
   * @param reservaId identificador de la reserva a cancelar
   * @returns Observable con la reserva actualizada
   */
  cancelarReserva(
    clienteId: number,
    reservaId: number,
  ): Observable<ReservaDTO> {
    return this.http.put<ReservaDTO>(
      `${this.apiUrl}/clientes/${clienteId}/reservas/${reservaId}/cancelar`,
      {},
    );
  }

  /**
   * Actualiza las fechas de una reserva.
   *
   * Permite cambiar las fechas de check-in/check-out y, opcionalmente,
   * el número de huéspedes. El backend valida que no haya solapamientos
   * con otras reservas de la misma habitación.
   *
   * @param clienteId identificador del cliente
   * @param reservaId identificador de la reserva a actualizar
   * @param datos DTO con nuevas fechas y datos opcionales
   * @returns Observable con la reserva actualizada
   */
  actualizarReserva(
    clienteId: number,
    reservaId: number,
    datos: ActualizarReservaDTO,
  ): Observable<ReservaDTO> {
    return this.http.put<ReservaDTO>(
      `${this.apiUrl}/clientes/${clienteId}/reservas/${reservaId}`,
      datos,
    );
  }
}
