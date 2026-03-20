import { Component, OnInit } from '@angular/core';
import { Habitacion } from '../../models/habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitaciones-lista',
  templateUrl: './habitaciones-lista.component.html',
  styleUrls: ['./habitaciones-lista.component.css']
})
export class HabitacionesListaComponent implements OnInit {
  habitaciones: Habitacion[] = [];

  constructor(private habitacionService: HabitacionService) {}

  ngOnInit(): void {
    this.habitaciones = this.habitacionService.getHabitaciones();
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
