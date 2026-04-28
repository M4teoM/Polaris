import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


/*
Información del cliente y su cuenta, retornada al buscar por número de habitación.
*/
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

/* Resultado retornado tras contactar exitosamente a un servicio,
incluye el servicio agregado y el nuevo total de la cuenta */

export interface ResultadoContratacionServicio {
  cuentaId: number;
  servicioId: number;
  servicioNombre: string;
  servicioPrecio: number;
  totalCuenta: number;
  mensaje: string;
}

/* Servicio de angular para operaciones del operador relacionado con contratar servicios del hotel,
se conecta con el backend de spring boot*/

@Injectable({ providedIn: 'root' })
export class OperadorServicioService {
  private readonly apiUrl = 'http://localhost:8080/api/operador/servicios';

  constructor(private http: HttpClient) {}


  /* Busca la informacion del cliente con reserva activa en su habitacion respectiva,
  Se ingresa el numero de habitacion para confirmar identidad del cliente */

  buscarInfoPorHabitacion$(
    numeroHabitacion: string,
  ): Observable<InfoHabitacionServicio> {
    return this.http.get<InfoHabitacionServicio>(
      `${this.apiUrl}/habitacion/${encodeURIComponent(numeroHabitacion)}`,
    );
  }


  /* Contrata un servicio para la estadia activa de una habitacion,
  ademas de agregar el servicio a la cuenta del cliente y retornar el total actualizado */

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
