import { Component, OnInit } from '@angular/core';
import { Habitacion } from '../../models/habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitaciones',
  templateUrl: './habitaciones.component.html',
  styleUrls: ['./habitaciones.component.css']
})
export class HabitacionesComponent implements OnInit {
  habitaciones: Habitacion[] = [];

  constructor(private habitacionService: HabitacionService) {}

  ngOnInit() {
    this.habitaciones = this.habitacionService.getHabitaciones();
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
