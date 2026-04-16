import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
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

  async ngOnInit(): Promise<void> {
    try {
      this.servicios = await firstValueFrom(
        this.servicioService.getServicios(),
      );
    } catch {
      this.servicios = [];
    }
  }

  toggleDetail(index: number, event: Event) {
    event.preventDefault();
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }
}
