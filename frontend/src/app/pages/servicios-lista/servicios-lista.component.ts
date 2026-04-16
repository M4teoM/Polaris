import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Servicio } from '../../models/servicio';
import { AuthService } from '../../services/auth.service';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-servicios-lista',
  templateUrl: './servicios-lista.component.html',
  styleUrls: ['./servicios-lista.component.css'],
})
export class ServiciosListaComponent implements OnInit {
  servicios: Servicio[] = [];
  serviciosFiltrados: Servicio[] = [];
  filtroActivo = 'all';
  categorias: string[] = [];
  panelActivo: 'admin' | 'operador' = 'admin';

  constructor(
    private servicioService: ServicioService,
    private authService: AuthService,
  ) {}

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  ngOnInit(): void {
    void this.recargarServicios();
  }

  filtrar(filtro: string): void {
    this.filtroActivo = filtro;
    this.serviciosFiltrados =
      filtro === 'all'
        ? this.servicios
        : this.servicios.filter((s) => s.categoria === filtro);
  }

  formatPrice(price: number): string {
    return `$${price.toLocaleString('es-CO')}`;
  }

  async eliminarDesdeTabla(id: number): Promise<void> {
    const confirmar = window.confirm('¿Eliminar este servicio?');
    if (!confirmar) {
      return;
    }

    try {
      await firstValueFrom(this.servicioService.delete$(id));
      await this.recargarServicios();
    } catch {
      // Keep UX simple in this public page replica.
      window.alert('No se pudo eliminar el servicio.');
    }
  }

  private async recargarServicios(): Promise<void> {
    try {
      this.servicios = await firstValueFrom(
        this.servicioService.getServicios(),
      );
      this.categorias = [
        ...new Set(this.servicios.map((s) => s.categoria)),
      ].filter((c) => !!c);
      this.filtrar(this.filtroActivo);
    } catch {
      this.servicios = [];
      this.serviciosFiltrados = [];
      this.categorias = [];
    }
  }
}
