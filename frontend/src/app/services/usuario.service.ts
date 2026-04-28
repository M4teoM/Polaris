import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientePerfilDTO } from '../models/cliente-perfil';

/**
 * Servicio Angular para gestionar información de perfil del cliente
 * a través del portal de usuario.
 *
 * Consume los endpoints REST del backend que inician con /api/portal-usuario/clientes.
 */
@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private readonly apiUrl = 'http://localhost:8080/api/portal-usuario';

  constructor(private http: HttpClient) {}

  /**
   * Obtiene el perfil del cliente con la información personal.
   *
   * @param clienteId identificador del cliente
   * @returns Observable con los datos del perfil en DTO plano
   */
  obtenerPerfil(clienteId: number): Observable<ClientePerfilDTO> {
    return this.http.get<ClientePerfilDTO>(
      `${this.apiUrl}/clientes/${clienteId}/perfil`,
    );
  }
}
