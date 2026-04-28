import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HabitacionFisica } from '../models/habitacion-fisica';


/*  Servicio angular para consumir los endpoints con REST de habitaciones fisicas,
se comunica con el backend del spring boot */


@Injectable({
  providedIn: 'root',
})
export class HabitacionFisicaService {
  private readonly apiUrl = 'http://localhost:8080/api/habitaciones-fisicas';

  constructor(private http: HttpClient) {}


  /* Obtiene la lista completa de habitaciones físicas. */

  getHabitaciones$(): Observable<HabitacionFisica[]> {
    return this.http.get<HabitacionFisica[]>(this.apiUrl);
  }


  /* Crea una nueva habitación física. */

  crearHabitacion$(
    habitacion: Omit<HabitacionFisica, 'id'>,
  ): Observable<HabitacionFisica> {
    return this.http.post<HabitacionFisica>(this.apiUrl, habitacion);
  }


  /* Actualiza los datos de una habitación física existente por su ID. */

  actualizarHabitacion$(
    id: number,
    cambios: Omit<HabitacionFisica, 'id'>,
  ): Observable<HabitacionFisica> {
    return this.http.put<HabitacionFisica>(`${this.apiUrl}/${id}`, cambios);
  }


  /* Elimina una habitación física por su ID. */

  eliminarHabitacion$(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
