import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.habitacionService.getHabitacionById(id).subscribe({
      next: data => { this.habitacion = data; },
      error: () => { this.router.navigate(['/habitaciones']); }
    });
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}