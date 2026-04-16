import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { SuscripcionService } from '../../services/suscripcion.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent {
  nombre = '';
  email = '';
  mensaje = '';
  tipoMensaje: 'ok' | 'error' | '' = '';
  enviando = false;

  constructor(private suscripcionService: SuscripcionService) {}

  async suscribirse(): Promise<void> {
    this.mensaje = '';
    this.tipoMensaje = '';

    const nombre = this.nombre.trim();
    const email = this.email.trim().toLowerCase();

    if (!nombre) {
      this.tipoMensaje = 'error';
      this.mensaje = 'Ingresa tu nombre.';
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      this.tipoMensaje = 'error';
      this.mensaje = 'Ingresa un correo válido.';
      return;
    }

    this.enviando = true;

    try {
      await firstValueFrom(this.suscripcionService.crear(nombre, email));
      this.tipoMensaje = 'ok';
      this.mensaje = '¡Suscripción registrada con éxito!';
      this.nombre = '';
      this.email = '';
    } catch (err: any) {
      this.tipoMensaje = 'error';
      this.mensaje =
        err?.error?.error || 'No se pudo registrar la suscripción.';
    } finally {
      this.enviando = false;
    }
  }
}
