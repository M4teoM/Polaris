import { Injectable } from '@angular/core';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser: Cliente | null = null;

  login(correo: string, contrasena: string): boolean {
    // TODO: Conectar con API real de Spring Boot
    // Por ahora simulamos un login exitoso
    if (correo && contrasena) {
      this.currentUser = {
        id: 1,
        nombre: 'Usuario',
        apellido: 'Demo',
        correo: correo
      };
      return true;
    }
    return false;
  }

  logout(): void {
    this.currentUser = null;
  }

  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  getCurrentUser(): Cliente | null {
    return this.currentUser;
  }

  register(cliente: Cliente): boolean {
    // TODO: Conectar con API real
    console.log('Registrando usuario:', cliente);
    return true;
  }
}
