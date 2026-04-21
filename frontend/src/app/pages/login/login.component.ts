import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  correo = '';
  contrasena = '';
  errorMessage = '';
  infoMessage =
    'Accesos: admin@polaris.com / admin123, operario@polaris.com / operario123';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  /**
   * Intenta autenticar al usuario y redirige al inicio si es exitoso.
   */
  async onLogin(): Promise<void> {
    this.errorMessage = '';

    try {
      const ok = await firstValueFrom(
        this.authService.login(this.correo, this.contrasena),
      );

      if (ok) {
        if (this.authService.isAdmin()) {
          this.router.navigate(['/admin']);
          return;
        }

        if (this.authService.isOperador()) {
          this.router.navigate(['/operador']);
          return;
        }

        this.router.navigate(['/clientes/ver']);
        return;
      }

      this.errorMessage = 'Credenciales incorrectas';
    } catch {
      this.errorMessage =
        'No se pudo iniciar sesión. Revisa si el backend está encendido.';
    }
  }
}
