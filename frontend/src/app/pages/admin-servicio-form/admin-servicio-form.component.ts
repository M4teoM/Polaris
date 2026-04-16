import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Servicio } from '../../models/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-admin-servicio-form',
  templateUrl: './admin-servicio-form.component.html',
  styleUrls: ['./admin-servicio-form.component.css'],
})
export class AdminServicioFormComponent implements OnInit {
  modoEdicion = false;
  idEdicion: number | null = null;
  errorGeneral = '';

  form: Omit<Servicio, 'id'> = {
    nombre: '',
    descripcion: '',
    descripcionDetallada: '',
    precio: 0,
    imagenUrl: '',
    categoria: '',
    duracion: '',
    horario: '',
    incluye: '',
    destacados: '',
    icono: '',
  };

  categorias = [
    'Aventura',
    'Atención',
    'Bienestar',
    'Deporte',
    'Experiencias',
    'Gastronomía',
    'Negocios',
    'Lujo',
    'Turismo',
  ];
  iconos = ['star', 'utensils', 'briefcase', 'swim', 'user', 'coffee'];

  constructor(
    private servicioService: ServicioService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    void this.cargarServicioEnEdicion();
  }

  private async cargarServicioEnEdicion(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.modoEdicion = true;
      this.idEdicion = +id;
      try {
        const servicio = await firstValueFrom(
          this.servicioService.getServicioById(this.idEdicion),
        );
        this.form = {
          nombre: servicio.nombre,
          descripcion: servicio.descripcion,
          descripcionDetallada: servicio.descripcionDetallada ?? '',
          precio: servicio.precio,
          imagenUrl: servicio.imagenUrl,
          categoria: servicio.categoria,
          duracion: servicio.duracion ?? '',
          horario: servicio.horario ?? '',
          incluye: servicio.incluye ?? '',
          destacados: servicio.destacados ?? '',
          icono: servicio.icono ?? '',
        };
      } catch {
        this.errorGeneral = 'No se pudo cargar el servicio a editar.';
      }
    }
  }

  get tituloPagina(): string {
    return this.modoEdicion ? 'Editar Servicio' : 'Nuevo Servicio';
  }
  get labelBoton(): string {
    return this.modoEdicion ? 'Guardar cambios' : 'Crear Servicio';
  }

  async guardar(): Promise<void> {
    this.errorGeneral = '';
    if (!this.form.nombre.trim()) {
      this.errorGeneral = 'El nombre es obligatorio.';
      return;
    }
    if (!this.form.descripcion.trim()) {
      this.errorGeneral = 'La descripción es obligatoria.';
      return;
    }
    if (!this.form.categoria.trim()) {
      this.errorGeneral = 'La categoría es obligatoria.';
      return;
    }
    if (this.form.precio < 0) {
      this.errorGeneral = 'El precio no puede ser negativo.';
      return;
    }

    if (this.modoEdicion && this.idEdicion !== null) {
      try {
        await firstValueFrom(
          this.servicioService.update(this.idEdicion, this.form),
        );
        this.router.navigate(['/admin/servicios']);
      } catch {
        this.errorGeneral = 'No se pudo actualizar el servicio.';
      }
    } else {
      try {
        await firstValueFrom(this.servicioService.create(this.form));
        this.router.navigate(['/admin/servicios']);
      } catch {
        this.errorGeneral = 'No se pudo crear el servicio.';
      }
    }
  }

  cancelar(): void {
    this.router.navigate(['/admin/servicios']);
  }
}
