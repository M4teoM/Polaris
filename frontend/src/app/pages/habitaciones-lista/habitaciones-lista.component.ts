import { Component, OnInit } from '@angular/core';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitaciones-lista',
  templateUrl: './habitaciones-lista.component.html',
  styleUrls: ['./habitaciones-lista.component.css'],
})
export class HabitacionesListaComponent implements OnInit {
  habitaciones: TipoHabitacion[] = [];

  constructor(private habitacionService: HabitacionService) {}

  ngOnInit(): void {
    this.habitacionService.getHabitaciones().subscribe(data => {
      this.habitaciones = data;
    });
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}