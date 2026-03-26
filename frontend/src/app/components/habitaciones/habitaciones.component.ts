import { Component, OnInit } from '@angular/core';
import { Habitacion } from '../../models/habitacion';
import { HabitacionService } from '../../services/habitacion.service';

@Component({
  selector: 'app-habitaciones',
  templateUrl: './habitaciones.component.html',
  styleUrls: ['./habitaciones.component.css'],
})
export class HabitacionesComponent implements OnInit {
  habitaciones: Habitacion[] = [];

  constructor(private habitacionService: HabitacionService) {}

  /**
   * Carga el listado de habitaciones al inicializar el componente.
   */
  ngOnInit() {
    this.habitaciones = this.habitacionService.getHabitaciones();
  }

  /**
   * Formatea un precio para mostrarlo en la vista.
   * @param price Precio base.
   * @returns Precio formateado para interfaz.
   */
  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
