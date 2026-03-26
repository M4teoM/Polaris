import { Component, HostListener } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  isScrolled = false;
  isMenuOpen = false;

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
}
