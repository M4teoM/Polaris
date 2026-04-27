import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ReservaHabitacion } from '../../models/reserva-habitacion';
import { ReservaService } from '../../services/reserva.service';

@Component({
  selector: 'app-reserva-detalle',
  templateUrl: './reserva-detalle.component.html',
  styleUrls: ['./reserva-detalle.component.css'],
})
export class ReservaDetalleComponent implements OnInit {
  reserva: ReservaHabitacion | null = null;
  cargando = false;
  error = '';
  returnUrl = '/admin/reservas';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly reservaService: ReservaService,
  ) {}

  ngOnInit(): void {
    const qpReturn = this.route.snapshot.queryParamMap.get('returnUrl');
    if (qpReturn && qpReturn.startsWith('/')) {
      this.returnUrl = qpReturn;
    }

    void this.cargarDetalle();
  }

  async cargarDetalle(): Promise<void> {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.error = 'ID de reserva inválido.';
      return;
    }

    this.cargando = true;
    this.error = '';
    try {
      this.reserva = await firstValueFrom(this.reservaService.getReservaById$(id));
    } catch {
      this.error = 'No se pudo cargar el detalle de la reserva.';
    } finally {
      this.cargando = false;
    }
  }
}
