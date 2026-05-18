import { Component } from '@angular/core';
import { Router }    from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  correo:       string  = '';
  contrasena:   string  = '';
  errorMessage: string  = '';
  infoMessage:  string  = '';
  cargando:     boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // El HTML llama onLogin() — internamente ejecuta login()
  onLogin(): void {
    this.login();
  }

  login(): void {
    this.errorMessage = '';
    this.infoMessage  = '';
    this.cargando     = true;

    this.authService.login(this.correo, this.contrasena).subscribe({
      next: (response) => {
        this.cargando = false;

        // Redirigimos según el rol que devolvió el backend
        const rol = response.rol;
        if      (rol === 'ROLE_ADMIN')    this.router.navigate(['/admin']);
        else if (rol === 'ROLE_OPERARIO') this.router.navigate(['/operador']);
        else if (rol === 'ROLE_CLIENTE')  this.router.navigate(['/portal']);
        else                              this.router.navigate(['/']);
      },
      error: () => {
        this.cargando     = false;
        this.errorMessage = 'Correo o contraseña incorrectos';
      }
    });
  }
}