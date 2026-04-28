import { Component, Input, Output, EventEmitter } from '@angular/core';

/**
 * Componente de barra lateral del Portal de Usuario.
 *
 * Muestra un menú lateral con opciones para navegar entre
 * perfil, reservas activas e historial.
 */
@Component({
  selector: 'app-user-sidebar',
  templateUrl: './user-sidebar.component.html',
  styleUrls: ['./user-sidebar.component.css'],
})
export class UserSidebarComponent {
  /**
   * Tab actualmente seleccionado ('perfil', 'activas', 'historial')
   */
  @Input() activeTab: string = 'perfil';

  /**
   * Evento que se emite cuando el usuario selecciona una pestaña
   */
  @Output() tabChanged = new EventEmitter<string>();

  /**
   * Emite el evento de cambio de pestaña cuando se hace clic en una opción
   *
   * @param tabName nombre de la pestaña seleccionada
   */
  selectTab(tabName: string): void {
    this.tabChanged.emit(tabName);
  }
}
