import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
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

  async ngOnInit(): Promise<void> {
    try {
      this.habitaciones = await firstValueFrom(
        this.habitacionService.getHabitaciones(),
      );
    } catch {
      this.habitaciones = [];
    }
  }

  formatPrice(price: number): string {
    return this.habitacionService.formatPrice(price);
  }
}
