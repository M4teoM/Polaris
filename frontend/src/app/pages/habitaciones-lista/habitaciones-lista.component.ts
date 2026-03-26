import { Component, OnInit } from '@angular/core';
import { Habitacion } from '../../models/habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitaciones-lista',
  templateUrl: './habitaciones-lista.component.html',
  styleUrls: ['./habitaciones-lista.component.css'],
})
export class HabitacionesListaComponent implements OnInit {
  habitaciones: Habitacion[] = [];

  constructor(private habitacionService: HabitacionService) {}

  /**
   * Carga el listado de habitaciones para la vista de catálogo.
   */
  ngOnInit(): void {
    this.habitaciones = this.habitacionService.getHabitaciones();
  }

  /**
   * Da formato monetario a los precios mostrados en la lista.
   * @param price Precio a formatear.
   * @returns Texto de precio formateado.
   */
  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
