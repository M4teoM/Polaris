import { Injectable } from '@angular/core';
import { Cliente } from '../models/cliente';
import { ClienteService } from './cliente.service';
import { Observable, map, of, tap } from 'rxjs';

export type UserRole = 'admin' | 'cliente';

interface SessionUser {
  role: UserRole;
  nombre: string;
  correo: string;
  cliente?: Cliente;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private static readonly STORAGE_KEY = 'polaris_session_user';

  private currentUser: SessionUser | null = null;

  private readonly adminCredentials = {
    correo: 'admin@polaris.com',
    contrasena: 'admin123',
    nombre: 'Administrador',
  };

  constructor(private clienteService: ClienteService) {
    this.restoreSession();
  }

  /**
   * Simula el inicio de sesión y guarda el usuario actual en memoria.
   * @param correo Correo del usuario.
   * @param contrasena Contraseña del usuario.
   * @returns true si los datos básicos existen; false en caso contrario.
   */
  login(correo: string, contrasena: string): Observable<boolean> {
    if (
      correo.toLowerCase() === this.adminCredentials.correo.toLowerCase() &&
      contrasena === this.adminCredentials.contrasena
    ) {
      this.currentUser = {
        role: 'admin',
        nombre: this.adminCredentials.nombre,
        correo: this.adminCredentials.correo,
      };
      this.persistSession();
      return of(true);
    }

    return this.clienteService.getClientes$().pipe(
      map((clientes) =>
        clientes.find(
          (c) =>
            c.correo.toLowerCase() === correo.toLowerCase() &&
            (c.contrasena || '') === contrasena,
        ),
      ),
      tap((cliente) => {
        if (!cliente) {
          this.currentUser = null;
          this.clearSession();
          return;
        }

        this.currentUser = {
          role: 'cliente',
          nombre: `${cliente.nombre} ${cliente.apellido}`.trim(),
          correo: cliente.correo,
          cliente,
        };
        this.persistSession();
      }),
      map((cliente) => !!cliente),
    );
  }

  /**
   * Cierra la sesión eliminando el usuario autenticado actual.
   */
  logout(): void {
    this.currentUser = null;
    this.clearSession();
  }

  /**
   * Informa si hay un usuario activo en la sesión local.
   * @returns true cuando existe usuario autenticado; false cuando no.
   */
  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  isAdmin(): boolean {
    return this.currentUser?.role === 'admin';
  }

  isCliente(): boolean {
    return this.currentUser?.role === 'cliente';
  }

  getRole(): UserRole | null {
    return this.currentUser?.role ?? null;
  }

  getDisplayName(): string {
    return this.currentUser?.nombre || 'Usuario';
  }

  /**
   * Obtiene el usuario autenticado actualmente.
   * @returns Cliente autenticado o null si no hay sesión.
   */
  getCurrentUser(): Cliente | null {
    return this.currentUser?.cliente || null;
  }

  updateClienteSession(cliente: Cliente): void {
    if (!this.currentUser || this.currentUser.role !== 'cliente') {
      return;
    }

    this.currentUser = {
      role: 'cliente',
      nombre: `${cliente.nombre} ${cliente.apellido}`.trim(),
      correo: cliente.correo,
      cliente,
    };
    this.persistSession();
  }

  /**
   * Simula el registro de un cliente.
   * @param cliente Datos del cliente a registrar.
   * @returns true mientras el flujo simulado sea exitoso.
   */
  register(cliente: Cliente): Observable<Cliente> {
    return this.clienteService
      .crearCliente$({
        nombre: cliente.nombre,
        apellido: cliente.apellido,
        correo: cliente.correo,
        contrasena: cliente.contrasena,
        cedula: cliente.cedula,
        telefono: cliente.telefono,
      })
      .pipe(
        tap((creado) => {
          this.currentUser = {
            role: 'cliente',
            nombre: `${creado.nombre} ${creado.apellido}`.trim(),
            correo: creado.correo,
            cliente: creado,
          };
          this.persistSession();
        }),
      );
  }

  private persistSession(): void {
    if (!this.currentUser) {
      return;
    }
    localStorage.setItem(
      AuthService.STORAGE_KEY,
      JSON.stringify(this.currentUser),
    );
  }

  private restoreSession(): void {
    const raw = localStorage.getItem(AuthService.STORAGE_KEY);
    if (!raw) {
      return;
    }

    try {
      this.currentUser = JSON.parse(raw) as SessionUser;
    } catch {
      this.currentUser = null;
      this.clearSession();
    }
  }

  private clearSession(): void {
    localStorage.removeItem(AuthService.STORAGE_KEY);
  }
}
