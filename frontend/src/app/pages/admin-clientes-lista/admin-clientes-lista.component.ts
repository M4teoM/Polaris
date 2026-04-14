import { Component, OnInit } from '@angular/core';
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

  form: Omit<Cliente, 'id'> = this.getEmptyForm();
  editandoId: number | null = null;

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.getClientes().subscribe((data) => {
      this.clientes = data;
    });
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

  guardar(): void {
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
      this.clienteService
        .actualizarCliente(this.editandoId, this.form)
        .subscribe({
          next: () => {
            this.mensajeExito = 'Cliente actualizado correctamente.';
            this.cancelarEdicion();
            this.cargarClientes();
          },
          error: (err) => {
            this.errorGeneral =
              err?.error?.error || 'No se pudo actualizar el cliente.';
          },
        });
      return;
    }

    this.clienteService.crearCliente(this.form).subscribe({
      next: () => {
        this.mensajeExito = 'Cliente creado correctamente.';
        this.form = this.getEmptyForm();
        this.cargarClientes();
      },
      error: (err) => {
        this.errorGeneral = err?.error?.error || 'No se pudo crear el cliente.';
      },
    });
  }

  confirmarEliminar(cliente: Cliente): void {
    this.clienteAEliminar = cliente;
    this.mostrarConfirmacion = true;
  }

  cancelarEliminar(): void {
    this.clienteAEliminar = null;
    this.mostrarConfirmacion = false;
  }

  ejecutarEliminar(): void {
    if (!this.clienteAEliminar?.id) {
      return;
    }

    this.clienteService.eliminarCliente(this.clienteAEliminar.id).subscribe({
      next: () => {
        this.mensajeExito = 'Cliente eliminado correctamente.';
        this.cancelarEliminar();
        this.cargarClientes();
      },
      error: () => {
        this.errorGeneral = 'No se pudo eliminar el cliente.';
        this.cancelarEliminar();
      },
    });
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
