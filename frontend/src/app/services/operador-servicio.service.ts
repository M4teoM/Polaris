import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface InfoHabitacionServicio {
  reservaId: number;
  habitacionId: number;
  habitacionNumero: string;
  clienteId: number;
  clienteNombre: string;
  clienteApellido: string;
  clienteCorreo: string;
  clienteCedula?: string;
  clienteTelefono?: string;
  cuentaId?: number;
  totalCuenta: number;
}

export interface ResultadoContratacionServicio {
  cuentaId: number;
  servicioId: number;
  servicioNombre: string;
  servicioPrecio: number;
  totalCuenta: number;
  mensaje: string;
}

@Injectable({ providedIn: 'root' })
export class OperadorServicioService {
  private readonly apiUrl = 'http://localhost:8080/api/operador/servicios';

  constructor(private http: HttpClient) {}

  buscarInfoPorHabitacion$(
    numeroHabitacion: string,
  ): Observable<InfoHabitacionServicio> {
    return this.http.get<InfoHabitacionServicio>(
      `${this.apiUrl}/habitacion/${encodeURIComponent(numeroHabitacion)}`,
    );
  }

  contratarServicio$(
    numeroHabitacion: string,
    servicioId: number,
  ): Observable<ResultadoContratacionServicio> {
    return this.http.post<ResultadoContratacionServicio>(
      `${this.apiUrl}/contratar`,
      { numeroHabitacion, servicioId },
    );
  }
}
