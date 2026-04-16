import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitacion-detalle',
  templateUrl: './habitacion-detalle.component.html',
  styleUrls: ['./habitacion-detalle.component.css'],
})
export class HabitacionDetalleComponent implements OnInit {
  habitacion: TipoHabitacion | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private habitacionService: HabitacionService,
  ) {}

  ngOnInit(): void {
    void this.cargarHabitacion();
  }

  private async cargarHabitacion(): Promise<void> {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    try {
      this.habitacion = await firstValueFrom(
        this.habitacionService.getHabitacionById(id),
      );
    } catch {
      this.router.navigate(['/habitaciones']);
    }
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
