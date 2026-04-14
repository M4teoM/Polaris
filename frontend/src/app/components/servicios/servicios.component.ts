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

  ngOnInit() {
    this.servicioService.getServicios().subscribe(data => {
      this.servicios = data;
    });
  }

  toggleDetail(index: number, event: Event) {
    event.preventDefault();
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }
}