import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Cliente } from '../../models/cliente';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-admin-clientes-lista',
  templateUrl: './admin-clientes-lista.component.html',
  styleUrls: ['./admin-clientes-lista.component.css'],
})
export class AdminClientesListaComponent implements OnInit {
  clientes: Cliente[] = [];
  mensajeExito = '';
  errorGeneral = '';
  clienteAEliminar: Cliente | null = null;
  mostrarConfirmacion = false;
  mostrarConfirmacionForzada = false;
  motivoBloqueoEliminacion = '';

  form: Omit<Cliente, 'id'> = this.getEmptyForm();
  editandoId: number | null = null;

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    void this.cargarClientes();
  }

  async cargarClientes(): Promise<void> {
    try {
      this.clientes = await firstValueFrom(this.clienteService.getClientes());
    } catch {
      this.errorGeneral = 'No se pudieron cargar los clientes.';
    }
  }

  editar(cliente: Cliente): void {
    this.editandoId = cliente.id || null;
    this.form = {
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      correo: cliente.correo,
      contrasena: cliente.contrasena || '',
      cedula: cliente.cedula || '',
      telefono: cliente.telefono || '',
    };
    this.errorGeneral = '';
  }

  async guardar(): Promise<void> {
    this.errorGeneral = '';
    if (
      !this.form.nombre.trim() ||
      !this.form.apellido.trim() ||
      !this.form.correo.trim()
    ) {
      this.errorGeneral = 'Nombre, apellido y correo son obligatorios.';
      return;
    }

    if (this.editandoId !== null) {
      try {
        await firstValueFrom(
          this.clienteService.actualizarCliente(this.editandoId, this.form),
        );
        this.mensajeExito = 'Cliente actualizado correctamente.';
        this.cancelarEdicion();
        await this.cargarClientes();
      } catch (err: any) {
        this.errorGeneral =
          err?.error?.error || 'No se pudo actualizar el cliente.';
      }
      return;
    }

    try {
      await firstValueFrom(this.clienteService.crearCliente(this.form));
      this.mensajeExito = 'Cliente creado correctamente.';
      this.form = this.getEmptyForm();
      await this.cargarClientes();
    } catch (err: any) {
      this.errorGeneral = err?.error?.error || 'No se pudo crear el cliente.';
    }
  }

  confirmarEliminar(cliente: Cliente): void {
    this.clienteAEliminar = cliente;
    this.mostrarConfirmacion = true;
    this.mostrarConfirmacionForzada = false;
    this.motivoBloqueoEliminacion = '';
  }

  cancelarEliminar(): void {
    this.clienteAEliminar = null;
    this.mostrarConfirmacion = false;
    this.mostrarConfirmacionForzada = false;
    this.motivoBloqueoEliminacion = '';
  }

  async ejecutarEliminar(): Promise<void> {
    if (!this.clienteAEliminar?.id) {
      return;
    }

    try {
      await firstValueFrom(
        this.clienteService.eliminarCliente(this.clienteAEliminar.id),
      );
      this.mensajeExito = 'Cliente eliminado correctamente.';
      this.cancelarEliminar();
      await this.cargarClientes();
    } catch (err: any) {
      this.motivoBloqueoEliminacion =
        err?.error?.error || 'No se pudo eliminar el cliente.';
      this.mostrarConfirmacion = false;
      this.mostrarConfirmacionForzada = true;
    }
  }

  async ejecutarEliminarForzado(): Promise<void> {
    if (!this.clienteAEliminar?.id) {
      return;
    }

    try {
      await firstValueFrom(
        this.clienteService.eliminarCliente(this.clienteAEliminar.id, true),
      );
      this.mensajeExito = 'Cliente eliminado de todos modos.';
      this.cancelarEliminar();
      await this.cargarClientes();
    } catch (err: any) {
      this.errorGeneral =
        err?.error?.error || 'No se pudo eliminar el cliente de forma forzada.';
      this.cancelarEliminar();
    }
  }

  cancelarEdicion(): void {
    this.editandoId = null;
    this.form = this.getEmptyForm();
  }

  private getEmptyForm(): Omit<Cliente, 'id'> {
    return {
      nombre: '',
      apellido: '',
      correo: '',
      contrasena: '',
      cedula: '',
      telefono: '',
    };
  }
}
