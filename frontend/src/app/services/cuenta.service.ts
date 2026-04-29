import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cuenta } from '../models/cuenta';

@Injectable({ providedIn: 'root' })
export class CuentaService {
  private readonly apiUrl = 'http://localhost:8080/api/operador/cuentas';

  constructor(private http: HttpClient) {}

  getCuentas$(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.apiUrl);
  }

  pagarItem$(itemId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/item/${itemId}/pagar`, {});
  }

  eliminarItem$(cuentaId: number, itemId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${cuentaId}/item/${itemId}`);
  }
}
