import { Component } from '@angular/core';
import { Router } from '@angular/router';
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
  infoMessage = 'Acceso admin: admin@polaris.com / admin123';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  /**
   * Intenta autenticar al usuario y redirige al inicio si es exitoso.
   */
  onLogin() {
    this.errorMessage = '';
    this.authService.login(this.correo, this.contrasena).subscribe({
      next: (ok) => {
        if (ok) {
          if (this.authService.isAdmin()) {
            this.router.navigate(['/admin/servicios']);
            return;
          }
          this.router.navigate(['/']);
          return;
        }
        this.errorMessage = 'Credenciales incorrectas';
      },
      error: () => {
        this.errorMessage =
          'No se pudo iniciar sesión. Revisa si el backend está encendido.';
      },
    });
  }
}
