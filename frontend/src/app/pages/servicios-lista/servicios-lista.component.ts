import { Component, OnInit } from '@angular/core';
import { Servicio } from '../../models/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-servicios-lista',
  templateUrl: './servicios-lista.component.html',
  styleUrls: ['./servicios-lista.component.css']
})
export class ServiciosListaComponent implements OnInit {
  servicios: Servicio[] = [];
  serviciosFiltrados: Servicio[] = [];
  filtroActivo = 'all';

  constructor(private servicioService: ServicioService) {}

  ngOnInit(): void {
    this.servicios = this.servicioService.getServicios();
    this.serviciosFiltrados = this.servicios;
  }

  filtrar(filtro: string): void {
    this.filtroActivo = filtro;
    if (filtro === 'all') {
      this.serviciosFiltrados = this.servicios;
    } else {
      this.serviciosFiltrados = this.servicios.filter(s => s.icono === filtro);
    }
  }
}
