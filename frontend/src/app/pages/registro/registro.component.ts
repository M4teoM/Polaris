import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { Cliente } from '../../models/cliente';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css'],
})
export class RegistroComponent {
  cliente: Cliente = {
    nombre: '',
    apellido: '',
    correo: '',
    contrasena: '',
  };

  confirmarContrasena = '';
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  registrar(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.cliente.nombre.trim() || !this.cliente.apellido.trim()) {
      this.errorMessage = 'Nombre y apellido son obligatorios.';
      return;
    }

    if (!this.cliente.correo.trim()) {
      this.errorMessage = 'El correo es obligatorio.';
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.cliente.correo)) {
      this.errorMessage = 'El correo no es válido.';
      return;
    }

    if (!this.cliente.contrasena || this.cliente.contrasena.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres.';
      return;
    }

    if (this.cliente.contrasena !== this.confirmarContrasena) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    this.authService.register(this.cliente).subscribe({
      next: () => {
        this.successMessage = 'Registro exitoso. Redirigiendo...';
        setTimeout(() => this.router.navigate(['/']), 900);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage =
          err?.error?.error ||
          'No se pudo registrar. Verifica backend/CORS e intenta de nuevo.';
      },
    });
  }
}
