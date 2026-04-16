import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
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
