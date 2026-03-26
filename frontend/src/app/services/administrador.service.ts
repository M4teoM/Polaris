import { Injectable } from '@angular/core';
import { Administrador } from '../models/administrador';

@Injectable({
  providedIn: 'root',
})
export class AdministradorService {
  private administradores: Administrador[] = [
    {
      id: 1,
      correo: 'admin@hotelpolaris.com',
      contrasena: '********',
    },
  ];

  /**
   * Retorna la lista completa de administradores registrados en memoria.
   * @returns Arreglo de administradores.
   */
  getAdministradores(): Administrador[] {
    return this.administradores;
  }

  /**
   * Busca un administrador por su identificador único.
   * @param id ID del administrador.
   * @returns El administrador encontrado o undefined si no existe.
   */
  getAdministradorById(id: number): Administrador | undefined {
    return this.administradores.find((a) => a.id === id);
  }

  /**
   * Valida credenciales de administrador usando el correo y la contraseña ingresados.
   * @param correo Correo del administrador.
   * @param contrasena Contraseña enviada en el formulario.
   * @returns El administrador asociado al correo o undefined si no coincide.
   */
  validarCredenciales(
    correo: string,
    contrasena: string,
  ): Administrador | undefined {
    return this.administradores.find((a) => a.correo === correo);
  }
}
