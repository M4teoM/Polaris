import { Component, OnInit } from '@angular/core';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitaciones',
  templateUrl: './habitaciones.component.html',
  styleUrls: ['./habitaciones.component.css'],
})
export class HabitacionesComponent implements OnInit {
  habitaciones: TipoHabitacion[] = [];

  constructor(private habitacionService: HabitacionService) {}

  ngOnInit() {
    this.habitacionService.getHabitaciones().subscribe(data => {
      this.habitaciones = data;
    });
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}