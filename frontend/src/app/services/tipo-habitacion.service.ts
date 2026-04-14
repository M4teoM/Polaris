import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoHabitacion } from '../models/tipo-habitacion';

@Injectable({ providedIn: 'root' })
export class TipoHabitacionService {
  private readonly apiUrl = 'http://localhost:8080/api/habitaciones';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TipoHabitacion[]> {
    return this.http.get<TipoHabitacion[]>(this.apiUrl);
  }

  getById(id: number): Observable<TipoHabitacion> {
    return this.http.get<TipoHabitacion>(`${this.apiUrl}/${id}`);
  }

  create(tipo: Omit<TipoHabitacion, 'id'>): Observable<TipoHabitacion> {
    return this.http.post<TipoHabitacion>(this.apiUrl, tipo);
  }

  update(id: number, tipo: Omit<TipoHabitacion, 'id'>): Observable<TipoHabitacion> {
    return this.http.put<TipoHabitacion>(`${this.apiUrl}/${id}`, tipo);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  formatPrice(price: number): string {
    return '$' + price.toLocaleString('es-CO');
  }
}