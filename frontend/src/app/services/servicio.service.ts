import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from '../models/servicio';

@Injectable({ providedIn: 'root' })
export class ServicioService {
  private readonly apiUrl = 'http://localhost:8080/api/servicios';

  constructor(private http: HttpClient) {}

  getServicios(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(this.apiUrl);
  }

  getServicioById(id: number): Observable<Servicio> {
    return this.http.get<Servicio>(`${this.apiUrl}/${id}`);
  }

  create(servicio: Omit<Servicio, 'id'>): Observable<Servicio> {
    return this.http.post<Servicio>(this.apiUrl, servicio);
  }

  createServicio(servicio: Omit<Servicio, 'id'>): Observable<Servicio> {
    return this.create(servicio);
  }

  update(id: number, servicio: Omit<Servicio, 'id'>): Observable<Servicio> {
    return this.http.put<Servicio>(`${this.apiUrl}/${id}`, servicio);
  }

  updateServicio(servicio: Servicio): Observable<Servicio> {
    return this.update(servicio.id, servicio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  deleteServicio(id: number): Observable<void> {
    return this.delete(id);
  }
}