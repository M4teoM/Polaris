import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoHabitacion } from '../models/tipo-habitacion';


/* Servicio Angular para consumir los endpoints REST de tipos de habitación,
se conecta con el backend de spring boot*/
@Injectable({ providedIn: 'root' })
export class TipoHabitacionService {
  private readonly apiUrl = 'http://localhost:8080/api/habitaciones';

  constructor(private http: HttpClient) {}


  /* Obtiene la lista completa de tipos de habitación. */

  getAll(): Observable<TipoHabitacion[]> {
    return this.getAll$();
  }
  

  getAll$(): Observable<TipoHabitacion[]> {
    return this.http.get<TipoHabitacion[]>(this.apiUrl);
  }


  /* Obtiene un tipo de habitación por su ID. */

  getById(id: number): Observable<TipoHabitacion> {
    return this.getById$(id);
  }

  getById$(id: number): Observable<TipoHabitacion> {
    return this.http.get<TipoHabitacion>(`${this.apiUrl}/${id}`);
  }



  /* Crea un nuevo tipo de habitación. */
  
  create(tipo: Omit<TipoHabitacion, 'id'>): Observable<TipoHabitacion> {
    return this.create$(tipo);
  }

  create$(tipo: Omit<TipoHabitacion, 'id'>): Observable<TipoHabitacion> {
    return this.http.post<TipoHabitacion>(this.apiUrl, tipo);
  }


  /* Actualiza un tipo de habitación existente por su ID. */

  update(
    id: number,
    tipo: Omit<TipoHabitacion, 'id'>,
  ): Observable<TipoHabitacion> {
    return this.update$(id, tipo);
  }

  update$(
    id: number,
    tipo: Omit<TipoHabitacion, 'id'>,
  ): Observable<TipoHabitacion> {
    return this.http.put<TipoHabitacion>(`${this.apiUrl}/${id}`, tipo);
  }


  /* Se elimina el tipo de habitacion por su ID,
  Si force=true, elimina en cascada las habitaciones reservas y cuentas asociadas */

  delete(id: number, force = false): Observable<void> {
    return this.delete$(id, force);
  }
  
  delete$(id: number, force = false): Observable<void> {
    const url = force
      ? `${this.apiUrl}/${id}?force=true`
      : `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }

  /* Se la un precio en pesos colombianos */
  formatPrice(price: number): string {
    return '$' + price.toLocaleString('es-CO');
  }
}
