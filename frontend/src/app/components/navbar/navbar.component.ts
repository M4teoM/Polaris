import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  isScrolled = false;
  isMenuOpen = false;

  constructor(
    public authService: AuthService,
    private router: Router,
  ) {}

  @HostListener('window:scroll')
  /**
   * Cambia el estado visual del navbar según la posición de scroll.
   */
  onScroll() {
    this.isScrolled = window.scrollY > 100;
  }

  /**
   * Alterna la apertura/cierre del menú móvil.
   */
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  /**
   * Cierra forzosamente el menú móvil.
   */
  closeMenu() {
    this.isMenuOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.closeMenu();
    this.router.navigate(['/']);
  }
}
