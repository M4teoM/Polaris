import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

/**
 * Componente principal del Portal de Usuario.
 *
 * Orquesta la visualización de los subcomponentes (perfil, reservas activas, historial)
 * y gestiona la navegación entre pestañas usando una barra lateral.
 */
@Component({
  selector: 'app-portal-usuario',
  templateUrl: './portal-usuario.component.html',
  styleUrls: ['./portal-usuario.component.css'],
})
export class PortalUsuarioComponent implements OnInit {
  /**
   * Pestaña actualmente visible ('perfil', 'activas', 'historial')
   */
  activeTab: string = 'perfil';

  /**
   * ID del cliente obtenido de la ruta
   */
  clienteId: number = 0;

  constructor(private route: ActivatedRoute) {}

  /**
   * Inicializa el componente extrayendo el clienteId de la ruta
   */
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.clienteId = params['clienteId'];
    });
  }

  /**
   * Cambia la pestaña activa cuando se hace clic en el menú lateral
   *
   * @param tabName nombre de la pestaña a mostrar
   */
  onTabChanged(tabName: string): void {
    this.activeTab = tabName;
  }
}
