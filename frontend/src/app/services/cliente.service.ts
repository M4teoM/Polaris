import { Injectable } from '@angular/core';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private clientes: Cliente[] = [
    {
      id: 1,
      nombre: 'Carlos',
      apellido: 'Rodríguez',
      correo: 'carlos.rodriguez@email.com',
      contrasena: '********'
    },
    {
      id: 2,
      nombre: 'María',
      apellido: 'González',
      correo: 'maria.gonzalez@email.com',
      contrasena: '********'
    },
    {
      id: 3,
      nombre: 'Juan',
      apellido: 'Martínez',
      correo: 'juan.martinez@email.com',
      contrasena: '********'
    },
    {
      id: 4,
      nombre: 'Ana',
      apellido: 'López',
      correo: 'ana.lopez@email.com',
      contrasena: '********'
    },
    {
      id: 5,
      nombre: 'Pedro',
      apellido: 'Sánchez',
      correo: 'pedro.sanchez@email.com',
      contrasena: '********'
    }
  ];

  getClientes(): Cliente[] {
    return this.clientes;
  }

  getClienteById(id: number): Cliente | undefined {
    return this.clientes.find(c => c.id === id);
  }

  getClienteByCorreo(correo: string): Cliente | undefined {
    return this.clientes.find(c => c.correo === correo);
  }
}
