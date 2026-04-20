import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Cliente } from '../../models/cliente';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { ClienteService } from '../../services/cliente.service';
import { TipoHabitacionService } from '../../services/tipo-habitacion.service';
import { ReservaService } from '../../services/reserva.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reserva-crear',
  templateUrl: './reserva-crear.component.html',
  styleUrls: ['./reserva-crear.component.css'],
})
export class ReservaCrearComponent implements OnInit {
  clientes: Cliente[] = [];
  tipos: TipoHabitacion[] = [];
  clienteSesion: Cliente | null = null;

  form = {
    clienteId: 0,
    tipoHabitacionId: 0,
    fechaCheckIn: '',
    fechaCheckOut: '',
    numeroHuespedes: 1,
  };

  guardando = false;
  error = '';
  ok = '';
  returnUrl = '/reservas';

  constructor(
    private readonly clienteService: ClienteService,
    private readonly tipoService: TipoHabitacionService,
    private readonly reservaService: ReservaService,
    public readonly authService: AuthService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    const qpReturn = this.route.snapshot.queryParamMap.get('returnUrl');
    if (qpReturn && qpReturn.startsWith('/')) {
      this.returnUrl = qpReturn;
    }

    this.clienteSesion = this.authService.getCurrentUser();
    if (this.authService.isCliente() && this.clienteSesion?.id) {
      this.form.clienteId = this.clienteSesion.id;
    }
    void this.cargarCatalogos();
  }

  async cargarCatalogos(): Promise<void> {
    try {
      const tipos = await firstValueFrom(this.tipoService.getAll$());
      this.tipos = tipos;

      if (this.authService.isCliente() && this.clienteSesion?.id) {
        this.clientes = [this.clienteSesion];
      } else {
        this.clientes = await firstValueFrom(this.clienteService.getClientes$());
      }
    } catch {
      this.error = 'No se pudo cargar clientes/tipos de habitación.';
    }
  }

  async crear(): Promise<void> {
    this.error = '';
    this.ok = '';

    if (this.authService.isCliente() && this.clienteSesion?.id) {
      this.form.clienteId = this.clienteSesion.id;
    }

    if (
      !this.form.clienteId ||
      !this.form.tipoHabitacionId ||
      !this.form.fechaCheckIn ||
      !this.form.fechaCheckOut
    ) {
      this.error = 'Completa todos los campos obligatorios.';
      return;
    }

    if (this.form.fechaCheckOut <= this.form.fechaCheckIn) {
      this.error = 'La fecha de salida debe ser posterior a la de entrada.';
      return;
    }

    this.guardando = true;
    try {
      await firstValueFrom(this.reservaService.crear$(this.form));
      this.ok = 'Reserva creada correctamente.';
      setTimeout(() => this.router.navigateByUrl(this.returnUrl), 700);
    } catch (err) {
      const e = err as HttpErrorResponse;
      this.error = e?.error?.error || 'No se pudo crear la reserva.';
    } finally {
      this.guardando = false;
    }
  }
}
