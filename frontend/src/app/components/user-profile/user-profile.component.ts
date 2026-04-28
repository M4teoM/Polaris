import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientePerfilDTO } from '../../models/cliente-perfil';
import { UsuarioService } from '../../services/usuario.service';

/**
 * Componente que muestra el perfil del cliente en el Portal de Usuario.
 *
 * Carga la información personal del cliente desde el backend
 * y la presenta de forma clara y estructurada.
 */
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  /**
   * Datos del perfil del cliente
   */
  perfil: ClientePerfilDTO | null = null;

  /**
   * Indica si se están cargando los datos
   */
  cargando = false;

  /**
   * Mensaje de error si algo falla
   */
  error: string | null = null;

  /**
   * ID del cliente obtenido de la ruta
   */
  /**
   * ID del cliente (puede venir por ruta o por input)
   */
  @Input() clienteId: number = 0;

  constructor(
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
  ) {}

  /**
   * Inicializa el componente cargando el perfil del cliente
   */
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.clienteId = params['clienteId'];
      this.cargarPerfil();
    });
  }

  /**
   * Carga el perfil del cliente desde el backend
   */
  private cargarPerfil(): void {
    this.cargando = true;
    this.error = null;

    this.usuarioService.obtenerPerfil(this.clienteId).subscribe({
      next: (datos) => {
        this.perfil = datos;
        this.cargando = false;
      },
      error: (err) => {
        this.error =
          'No se pudo cargar el perfil. Por favor, intenta más tarde.';
        console.error('Error al cargar perfil:', err);
        this.cargando = false;
      },
    });
  }

  /**
   * Retorna el nombre completo del cliente
   */
  get nombreCompleto(): string {
    if (!this.perfil) return '';
    return `${this.perfil.nombre} ${this.perfil.apellido}`;
  }
}
