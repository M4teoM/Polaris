import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
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

  ngOnInit(): void {
    void this.cargarServicio();
  }

  private async cargarServicio(): Promise<void> {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    try {
      this.servicio = await firstValueFrom(
        this.servicioService.getServicioById(id),
      );
    } catch {
      this.router.navigate(['/servicios']);
    }
  }
}
