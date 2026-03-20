import { Injectable } from '@angular/core';
import { Operario } from '../models/operario';

@Injectable({
  providedIn: 'root'
})
export class OperarioService {

  private operarios: Operario[] = [
    {
      id: 1,
      correo: 'recepcion1@hotelpolaris.com',
      contrasena: '********',
      nombre: 'Laura Pérez',
      administradorId: 1
    },
    {
      id: 2,
      correo: 'recepcion2@hotelpolaris.com',
      contrasena: '********',
      nombre: 'Miguel Ángel Torres',
      administradorId: 1
    },
    {
      id: 3,
      correo: 'conserje@hotelpolaris.com',
      contrasena: '********',
      nombre: 'Roberto Gómez',
      administradorId: 1
    }
  ];

  getOperarios(): Operario[] {
    return this.operarios;
  }

  getOperarioById(id: number): Operario | undefined {
    return this.operarios.find(o => o.id === id);
  }

  getOperariosByAdmin(adminId: number): Operario[] {
    return this.operarios.filter(o => o.administradorId === adminId);
  }
}
