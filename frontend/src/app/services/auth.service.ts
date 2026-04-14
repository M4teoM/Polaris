import { Injectable } from '@angular/core';
import { Cliente } from '../models/cliente';
import { ClienteService } from './cliente.service';
import { Observable, map, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUser: Cliente | null = null;

  constructor(private clienteService: ClienteService) {}

  /**
   * Simula el inicio de sesión y guarda el usuario actual en memoria.
   * @param correo Correo del usuario.
   * @param contrasena Contraseña del usuario.
   * @returns true si los datos básicos existen; false en caso contrario.
   */
  login(correo: string, contrasena: string): Observable<boolean> {
    return this.clienteService.getClientes().pipe(
      map((clientes) =>
        clientes.find(
          (c) =>
            c.correo.toLowerCase() === correo.toLowerCase() &&
            (c.contrasena || '') === contrasena,
        ),
      ),
      tap((cliente) => {
        this.currentUser = cliente || null;
      }),
      map((cliente) => !!cliente),
    );
  }

  /**
   * Cierra la sesión eliminando el usuario autenticado actual.
   */
  logout(): void {
    this.currentUser = null;
  }

  /**
   * Informa si hay un usuario activo en la sesión local.
   * @returns true cuando existe usuario autenticado; false cuando no.
   */
  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  /**
   * Obtiene el usuario autenticado actualmente.
   * @returns Cliente autenticado o null si no hay sesión.
   */
  getCurrentUser(): Cliente | null {
    return this.currentUser;
  }

  /**
   * Simula el registro de un cliente.
   * @param cliente Datos del cliente a registrar.
   * @returns true mientras el flujo simulado sea exitoso.
   */
  register(cliente: Cliente): Observable<Cliente> {
    return this.clienteService.crearCliente({
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      correo: cliente.correo,
      contrasena: cliente.contrasena,
      cedula: cliente.cedula,
      telefono: cliente.telefono,
    }).pipe(
      tap((creado) => {
        this.currentUser = creado;
      }),
    );
  }
}
