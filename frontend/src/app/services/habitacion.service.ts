import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoHabitacion } from '../models/tipo-habitacion';

@Injectable({ providedIn: 'root' })
export class HabitacionService {
  private readonly apiUrl = 'http://localhost:8080/api/habitaciones';

  constructor(private http: HttpClient) {}

  getHabitaciones(): Observable<TipoHabitacion[]> {
    return this.http.get<TipoHabitacion[]>(this.apiUrl);
  }

  getHabitacionById(id: number): Observable<TipoHabitacion> {
    return this.http.get<TipoHabitacion>(`${this.apiUrl}/${id}`);
  }

  formatPrice(price: number): string {
    return '$' + price.toLocaleString('es-CO');
  }
}