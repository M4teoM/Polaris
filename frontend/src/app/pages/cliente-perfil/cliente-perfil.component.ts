import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Cliente } from '../../models/cliente';
import { AuthService } from '../../services/auth.service';
import { ClienteService } from '../../services/cliente.service';
import { ReservaService } from '../../services/reserva.service';

interface ReservaPerfil {
  id: number;
  fechaCheckIn: string;
  fechaCheckOut: string;
  estado: string;
  habitacionId?: number;
  habitacion?: {
    numero?: string;
    tipoHabitacion?: {
      nombre?: string;
    };
  };
}

@Component({
  selector: 'app-cliente-perfil',
  templateUrl: './cliente-perfil.component.html',
  styleUrls: ['./cliente-perfil.component.css'],
})
export class ClientePerfilComponent implements OnInit {
  cargando = true;
  editando = false;
  guardando = false;
  cancelandoReservaId: number | null = null;

  cliente: Cliente | null = null;
  reservas: ReservaPerfil[] = [];

  mensajeExito = '';
  errorGeneral = '';

  form: Omit<Cliente, 'id'> = {
    nombre: '',
    apellido: '',
    correo: '',
    contrasena: '',
    cedula: '',
    telefono: '',
  };

  constructor(
    private readonly authService: AuthService,
    private readonly clienteService: ClienteService,
    private readonly reservaService: ReservaService,
    private readonly router: Router,
  ) {}

  async ngOnInit(): Promise<void> {
    const clienteSesion = this.authService.getCurrentUser();

    if (!clienteSesion?.id) {
      this.router.navigate(['/login']);
      return;
    }

    await this.cargarPerfil(clienteSesion.id);
  }

  get avatar(): string {
    if (!this.cliente?.nombre) {
      return 'U';
    }

    return this.cliente.nombre.charAt(0).toUpperCase();
  }

  get clienteId(): number {
    return this.cliente?.id ?? 0;
  }

  activarEdicion(): void {
    this.editando = true;
    this.errorGeneral = '';
    this.mensajeExito = '';
  }

  cancelarEdicion(): void {
    this.editando = false;
    this.errorGeneral = '';
    this.form = this.mapFormDesdeCliente(this.cliente);
  }

  async guardarCambios(): Promise<void> {
    if (!this.cliente?.id || this.guardando) {
      return;
    }

    if (!this.form.nombre.trim() || !this.form.apellido.trim()) {
      this.errorGeneral = 'Nombre y apellido son obligatorios.';
      return;
    }

    if (!this.form.correo.trim() || !this.form.contrasena?.trim()) {
      this.errorGeneral = 'Correo y contraseña son obligatorios.';
      return;
    }

    this.guardando = true;
    this.errorGeneral = '';

    try {
      const actualizado = await firstValueFrom(
        this.clienteService.actualizarCliente$(this.cliente.id, {
          nombre: this.form.nombre.trim(),
          apellido: this.form.apellido.trim(),
          correo: this.form.correo.trim(),
          contrasena: this.form.contrasena.trim(),
          cedula: this.form.cedula?.trim() || '',
          telefono: this.form.telefono?.trim() || '',
        }),
      );

      this.cliente = actualizado;
      this.form = this.mapFormDesdeCliente(actualizado);
      this.editando = false;
      this.mensajeExito = 'Tu información fue actualizada correctamente.';
      this.authService.updateClienteSession(actualizado);
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error || 'No se pudo actualizar tu información.';
    } finally {
      this.guardando = false;
    }
  }

  async cancelarReserva(reserva: ReservaPerfil): Promise<void> {
    if (!this.cliente?.id || this.cancelandoReservaId !== null) {
      return;
    }

    const confirmar = window.confirm('¿Cancelar esta reserva?');
    if (!confirmar) {
      return;
    }

    this.cancelandoReservaId = reserva.id;
    this.errorGeneral = '';
    this.mensajeExito = '';

    try {
      await firstValueFrom(
        this.reservaService.cancelar$(reserva.id, this.cliente.id),
      );
      this.mensajeExito = 'Reserva cancelada correctamente.';
      await this.cargarReservas(this.cliente.id);
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error || 'No se pudo cancelar la reserva.';
    } finally {
      this.cancelandoReservaId = null;
    }
  }

  async eliminarCuenta(): Promise<void> {
    if (!this.cliente?.id) {
      return;
    }

    const confirmar = window.confirm(
      '¿Seguro que deseas eliminar tu cuenta? Esta acción no se puede deshacer.',
    );
    if (!confirmar) {
      return;
    }

    this.errorGeneral = '';

    try {
      await firstValueFrom(
        this.clienteService.eliminarCliente$(this.cliente.id),
      );
      this.authService.logout();
      this.router.navigate(['/']);
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error || 'No se pudo eliminar tu cuenta en este momento.';
    }
  }

  estadoClase(estado: string): string {
    return (estado || 'pendiente').toLowerCase();
  }

  formatFecha(fechaISO: string): string {
    if (!fechaISO) {
      return '-';
    }

    const partes = fechaISO.split('-');
    if (partes.length !== 3) {
      return fechaISO;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  habitacionLabel(reserva: ReservaPerfil): string {
    if (reserva.habitacion?.numero) {
      return `Hab. ${reserva.habitacion.numero}`;
    }

    if (reserva.habitacionId) {
      return `Hab. ${reserva.habitacionId}`;
    }

    return 'Sin asignar';
  }

  tipoLabel(reserva: ReservaPerfil): string {
    return reserva.habitacion?.tipoHabitacion?.nombre || '-';
  }

  private async cargarPerfil(clienteId: number): Promise<void> {
    this.cargando = true;
    this.errorGeneral = '';

    try {
      const [cliente, reservas] = await Promise.all([
        firstValueFrom(this.clienteService.getClienteById$(clienteId)),
        firstValueFrom(this.reservaService.getReservasByCliente$(clienteId)),
      ]);

      this.cliente = cliente;
      this.form = this.mapFormDesdeCliente(cliente);
      this.reservas = reservas as ReservaPerfil[];
      this.authService.updateClienteSession(cliente);
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error ||
        'No fue posible cargar tu perfil. Intenta de nuevo.';
    } finally {
      this.cargando = false;
    }
  }

  private async cargarReservas(clienteId: number): Promise<void> {
    this.reservas = (await firstValueFrom(
      this.reservaService.getReservasByCliente$(clienteId),
    )) as ReservaPerfil[];
  }

  private mapFormDesdeCliente(cliente: Cliente | null): Omit<Cliente, 'id'> {
    if (!cliente) {
      return {
        nombre: '',
        apellido: '',
        correo: '',
        contrasena: '',
        cedula: '',
        telefono: '',
      };
    }

    return {
      nombre: cliente.nombre || '',
      apellido: cliente.apellido || '',
      correo: cliente.correo || '',
      contrasena: cliente.contrasena || '',
      cedula: cliente.cedula || '',
      telefono: cliente.telefono || '',
    };
  }
}
