import { Injectable } from '@angular/core';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUser: Cliente | null = null;

  /**
   * Simula el inicio de sesión y guarda el usuario actual en memoria.
   * @param correo Correo del usuario.
   * @param contrasena Contraseña del usuario.
   * @returns true si los datos básicos existen; false en caso contrario.
   */
  login(correo: string, contrasena: string): boolean {
    // TODO: Conectar con API real de Spring Boot
    // Por ahora simulamos un login exitoso
    if (correo && contrasena) {
      this.currentUser = {
        id: 1,
        nombre: 'Usuario',
        apellido: 'Demo',
        correo: correo,
      };
      return true;
    }
    return false;
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
  register(cliente: Cliente): boolean {
    // TODO: Conectar con API real
    console.log('Registrando usuario:', cliente);
    return true;
  }
}
