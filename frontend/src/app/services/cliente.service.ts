import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private readonly apiUrl = 'http://localhost:8080/api/clientes';

  constructor(private http: HttpClient) {}

  getClientes(): Observable<Cliente[]> {
    return this.getClientes$();
  }

  getClientes$(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  getClienteById(id: number): Observable<Cliente> {
    return this.getClienteById$(id);
  }

  getClienteById$(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  crearCliente(cliente: Omit<Cliente, 'id'>): Observable<Cliente> {
    return this.crearCliente$(cliente);
  }

  crearCliente$(cliente: Omit<Cliente, 'id'>): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }

  actualizarCliente(
    id: number,
    cliente: Omit<Cliente, 'id'>,
  ): Observable<Cliente> {
    return this.actualizarCliente$(id, cliente);
  }

  actualizarCliente$(
    id: number,
    cliente: Omit<Cliente, 'id'>,
  ): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${id}`, cliente);
  }

  eliminarCliente(id: number, force = false): Observable<void> {
    return this.eliminarCliente$(id, force);
  }

  eliminarCliente$(id: number, force = false): Observable<void> {
    const url = force
      ? `${this.apiUrl}/${id}?force=true`
      : `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }
}
