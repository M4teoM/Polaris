import { Injectable } from '@angular/core';
import { Administrador } from '../models/administrador';

@Injectable({
  providedIn: 'root'
})
export class AdministradorService {

  private administradores: Administrador[] = [
    {
      id: 1,
      correo: 'admin@hotelpolaris.com',
      contrasena: '********'
    }
  ];

  getAdministradores(): Administrador[] {
    return this.administradores;
  }

  getAdministradorById(id: number): Administrador | undefined {
    return this.administradores.find(a => a.id === id);
  }

  validarCredenciales(correo: string, contrasena: string): Administrador | undefined {
    return this.administradores.find(a => a.correo === correo);
  }
}
