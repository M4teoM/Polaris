import { Component, OnInit } from '@angular/core';
import { Router }            from '@angular/router';
import { AuthService }       from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Al abrir la home, verificamos si ya hay un JWT guardado
    if (this.authService.isLoggedIn()) {
      const rol  = this.authService.getRol();
      const id   = this.authService.getUser()?.id;

      if      (rol === 'ROLE_ADMIN')    this.router.navigate(['/admin']);
      else if (rol === 'ROLE_OPERARIO') this.router.navigate(['/operador']);
      else if (rol === 'ROLE_CLIENTE')  this.router.navigate(['/portal-usuario', id]);
    }
    // Si no hay token simplemente muestra la landing page normalmente
  }
}