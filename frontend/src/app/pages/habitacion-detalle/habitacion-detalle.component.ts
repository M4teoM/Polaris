import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Habitacion } from '../../models/habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitacion-detalle',
  templateUrl: './habitacion-detalle.component.html',
  styleUrls: ['./habitacion-detalle.component.css']
})
export class HabitacionDetalleComponent implements OnInit {
  habitacion: Habitacion | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private habitacionService: HabitacionService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.habitacion = this.habitacionService.getHabitacionById(id);
    if (!this.habitacion) {
      this.router.navigate(['/habitaciones']);
    }
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
