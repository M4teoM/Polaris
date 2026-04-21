import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaHabitacion } from '../models/reserva-habitacion';

@Injectable({ providedIn: 'root' })
export class ReservaService {
  private readonly apiUrl = 'http://localhost:8080/api/reservas';

  constructor(private http: HttpClient) {}

  getReservas(): Observable<ReservaHabitacion[]> {
    return this.getReservas$();
  }

  getReservas$(): Observable<ReservaHabitacion[]> {
    return this.http.get<ReservaHabitacion[]>(this.apiUrl);
  }

  getReservaById(id: number): Observable<ReservaHabitacion> {
    return this.getReservaById$(id);
  }

  getReservaById$(id: number): Observable<ReservaHabitacion> {
    return this.http.get<ReservaHabitacion>(`${this.apiUrl}/${id}`);
  }

  getReservasByCliente(clienteId: number): Observable<ReservaHabitacion[]> {
    return this.getReservasByCliente$(clienteId);
  }

  getReservasByCliente$(clienteId: number): Observable<ReservaHabitacion[]> {
    return this.http.get<ReservaHabitacion[]>(
      `${this.apiUrl}/cliente/${clienteId}`,
    );
  }

  crear(body: {
    clienteId: number;
    tipoHabitacionId: number;
    fechaCheckIn: string;
    fechaCheckOut: string;
    numeroHuespedes: number;
  }): Observable<any> {
    return this.crear$(body);
  }

  crear$(body: {
    clienteId: number;
    tipoHabitacionId: number;
    fechaCheckIn: string;
    fechaCheckOut: string;
    numeroHuespedes: number;
  }): Observable<any> {
    return this.http.post(this.apiUrl, body);
  }

  update$(
    id: number,
    body: {
      fechaCheckIn: string;
      fechaCheckOut: string;
      numeroHuespedes: number;
      estado?: string;
    },
  ): Observable<ReservaHabitacion> {
    return this.http.put<ReservaHabitacion>(`${this.apiUrl}/${id}`, body);
  }

  delete$(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  cancelar(id: number, clienteId: number): Observable<any> {
    return this.cancelar$(id, clienteId);
  }

  cancelar$(id: number, clienteId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/cancelar/${id}`, { clienteId });
  }
}
