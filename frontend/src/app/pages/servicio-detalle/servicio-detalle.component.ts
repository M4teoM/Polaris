import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Servicio } from '../../models/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-servicio-detalle',
  templateUrl: './servicio-detalle.component.html',
  styleUrls: ['./servicio-detalle.component.css'],
})
export class ServicioDetalleComponent implements OnInit {
  servicio: Servicio | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private servicioService: ServicioService,
  ) {}

  /**
   * Obtiene el servicio por ID desde parámetros de ruta.
   */
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.servicio = this.servicioService.getServicioById(id);
    if (!this.servicio) {
      this.router.navigate(['/servicios']);
    }
  }
}
