import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Suscripcion } from '../models/suscripcion';

@Injectable({
  providedIn: 'root',
})
export class SuscripcionService {
  private readonly apiUrl = 'http://localhost:8080/api/suscripciones';

  constructor(private http: HttpClient) {}

  listar(): Observable<Suscripcion[]> {
    return this.http.get<Suscripcion[]>(this.apiUrl);
  }

  crear(nombre: string, email: string): Observable<Suscripcion> {
    return this.http.post<Suscripcion>(this.apiUrl, { nombre, email });
  }

  cancelar(id: number): Observable<Suscripcion> {
    return this.http.delete<Suscripcion>(`${this.apiUrl}/${id}`);
  }
}
