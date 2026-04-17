import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HabitacionFisica } from '../models/habitacion-fisica';

@Injectable({
  providedIn: 'root',
})
export class HabitacionFisicaService {
  private readonly apiUrl = 'http://localhost:8080/api/habitaciones-fisicas';

  constructor(private http: HttpClient) {}

  getHabitaciones$(): Observable<HabitacionFisica[]> {
    return this.http.get<HabitacionFisica[]>(this.apiUrl);
  }

  crearHabitacion$(
    habitacion: Omit<HabitacionFisica, 'id'>,
  ): Observable<HabitacionFisica> {
    return this.http.post<HabitacionFisica>(this.apiUrl, habitacion);
  }

  actualizarHabitacion$(
    id: number,
    cambios: Omit<HabitacionFisica, 'id'>,
  ): Observable<HabitacionFisica> {
    return this.http.put<HabitacionFisica>(`${this.apiUrl}/${id}`, cambios);
  }

  eliminarHabitacion$(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
