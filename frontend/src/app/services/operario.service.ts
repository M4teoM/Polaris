import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Operario } from '../models/operario';

@Injectable({
  providedIn: 'root',
})
export class OperarioService {
  private readonly apiUrl = 'http://localhost:8080/api/operarios';

  constructor(private http: HttpClient) {}

  /**
   * Retorna todos los operarios disponibles.
   * @returns Arreglo de operarios.
   */
  getOperarios$(): Observable<Operario[]> {
    return this.http.get<Operario[]>(this.apiUrl);
  }

  /**
   * Busca un operario por su ID.
   * @param id ID del operario.
   * @returns Operario encontrado o undefined.
   */
  login$(correo: string, contrasena: string): Observable<Operario> {
    return this.http.post<Operario>(`${this.apiUrl}/login`, {
      correo,
      contrasena,
    });
  }
}
