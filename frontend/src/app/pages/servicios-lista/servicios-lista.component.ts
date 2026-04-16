import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Servicio } from '../../models/servicio';
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

  constructor(private servicioService: ServicioService) {}

  ngOnInit(): void {
    void this.recargarServicios();
  }

  filtrar(filtro: string): void {
    this.filtroActivo = filtro;
    this.serviciosFiltrados =
      filtro === 'all'
        ? this.servicios
        : this.servicios.filter((s) => s.icono === filtro);
  }

  private async recargarServicios(): Promise<void> {
    try {
      this.servicios = await firstValueFrom(
        this.servicioService.getServicios(),
      );
      this.filtrar(this.filtroActivo);
    } catch {
      this.servicios = [];
      this.serviciosFiltrados = [];
    }
  }
}
