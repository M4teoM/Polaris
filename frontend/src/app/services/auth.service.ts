import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router }     from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface LoginResponse {
  token:  string;
  rol:    string;
  id:     number;
  nombre: string;
  correo: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl    = 'http://localhost:8080/api/auth';
  private TOKEN_KEY = 'polaris_token';
  private USER_KEY  = 'polaris_user';

  constructor(private http: HttpClient, private router: Router) {}

  // ── Autenticación ────────────────────────────────────────────────────────

  login(correo: string, contrasena: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, { correo, contrasena })
      .pipe(
        tap(response => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          localStorage.setItem(this.USER_KEY, JSON.stringify(response));
        })
      );
  }

  // Registro de cliente nuevo — llama al endpoint existente de clientes
  register(cliente: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/clientes', cliente);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/login']);
  }

  // ── Getters del token y usuario ──────────────────────────────────────────

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getUser(): LoginResponse | null {
    const u = localStorage.getItem(this.USER_KEY);
    return u ? JSON.parse(u) : null;
  }

  // Alias usado en algunos componentes existentes
  getCurrentUser(): LoginResponse | null {
    return this.getUser();
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getRol(): string | null {
    return this.getUser()?.rol ?? null;
  }

  // Nombre para mostrar en la navbar — "Samuel Tovar"
  getDisplayName(): string {
    return this.getUser()?.nombre ?? '';
  }

  // ── Verificadores de rol ─────────────────────────────────────────────────

  isAdmin():    boolean { return this.getRol() === 'ROLE_ADMIN'; }
  isOperario(): boolean { return this.getRol() === 'ROLE_OPERARIO'; }
  isCliente():  boolean { return this.getRol() === 'ROLE_CLIENTE'; }

  // Alias para compatibilidad con código existente que usaba isOperador()
  isOperador(): boolean { return this.isOperario(); }

  // ── Actualizar datos del usuario en sesión ───────────────────────────────

  // Usado en cliente-perfil cuando el usuario edita sus datos
  // Actualiza el localStorage sin tocar el token
  updateClienteSession(clienteActualizado: any): void {
    const user = this.getUser();
    if (!user) return;

    const updated: LoginResponse = {
      ...user,
      nombre: clienteActualizado.nombre
              ? `${clienteActualizado.nombre} ${clienteActualizado.apellido ?? ''}`.trim()
              : user.nombre,
      correo: clienteActualizado.correo ?? user.correo
    };

    localStorage.setItem(this.USER_KEY, JSON.stringify(updated));
  }
}