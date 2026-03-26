import { Component, OnInit } from '@angular/core';
import { Servicio } from '../../models/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-servicios',
  templateUrl: './servicios.component.html',
  styleUrls: ['./servicios.component.css'],
})
export class ServiciosComponent implements OnInit {
  servicios: Servicio[] = [];
  expandedIndex: number | null = null;

  constructor(private servicioService: ServicioService) {}

  /**
   * Carga el catálogo de servicios al iniciar el componente.
   */
  ngOnInit() {
    this.servicios = this.servicioService.getServicios();
  }

  /**
   * Expande o contrae el detalle del servicio seleccionado.
   * @param index Índice del servicio en la lista.
   * @param event Evento de clic para evitar navegación por defecto.
   */
  toggleDetail(index: number, event: Event) {
    event.preventDefault();
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }
}
