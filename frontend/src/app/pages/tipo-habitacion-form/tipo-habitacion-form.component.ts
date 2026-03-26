import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TipoHabitacion } from '../../models/tipo-habitacion';
import { TipoHabitacionService } from '../../services/tipo-habitacion.service';

@Component({
  selector: 'app-tipo-habitacion-form',
  templateUrl: './tipo-habitacion-form.component.html',
  styleUrls: ['./tipo-habitacion-form.component.css'],
})
export class TipoHabitacionFormComponent implements OnInit {
  modoEdicion = false;
  idEdicion: number | null = null;
  cargando = false;
  errorGeneral = '';

  form: Omit<TipoHabitacion, 'id'> = {
    nombre: '',
    descripcion: '',
    imagenUrl: '',
    precioPorNoche: 0,
    metrosCuadrados: 0,
    tipoCama: '',
    capacidad: 1,
  };

  tiposCama = ['King Size', 'Queen Size', 'Doble', 'Individual', 'Litera'];

  constructor(
    private tipoHabitacionService: TipoHabitacionService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  /**
   * Detecta si el formulario está en modo edición y precarga datos si aplica.
   */
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.modoEdicion = true;
      this.idEdicion = +id;
      const tipo = this.tipoHabitacionService.getById(this.idEdicion);
      if (tipo) {
        this.form = {
          nombre: tipo.nombre,
          descripcion: tipo.descripcion,
          imagenUrl: tipo.imagenUrl,
          precioPorNoche: tipo.precioPorNoche,
          metrosCuadrados: tipo.metrosCuadrados,
          tipoCama: tipo.tipoCama,
          capacidad: tipo.capacidad,
        };
      } else {
        this.errorGeneral = 'No se encontró el tipo de habitación.';
      }
    }
  }

  /**
   * Define el título dinámico según modo crear/editar.
   * @returns Título para la cabecera de la página.
   */
  get tituloPagina(): string {
    return this.modoEdicion
      ? 'Editar Tipo de Habitación'
      : 'Nuevo Tipo de Habitación';
  }

  /**
   * Define la etiqueta del botón principal de envío.
   * @returns Texto del botón de acción.
   */
  get labelBoton(): string {
    return this.modoEdicion ? 'Guardar cambios' : 'Crear Tipo';
  }

  /**
   * Valida el formulario y guarda (crear/editar) el tipo de habitación.
   */
  guardar(): void {
    this.errorGeneral = '';

    if (!this.form.nombre.trim()) {
      this.errorGeneral = 'El nombre es obligatorio.';
      return;
    }
    if (!this.form.descripcion.trim()) {
      this.errorGeneral = 'La descripción es obligatoria.';
      return;
    }
    if (!this.form.tipoCama) {
      this.errorGeneral = 'Selecciona un tipo de cama.';
      return;
    }
    if (this.form.precioPorNoche <= 0) {
      this.errorGeneral = 'El precio por noche debe ser mayor a 0.';
      return;
    }
    if (this.form.metrosCuadrados <= 0) {
      this.errorGeneral = 'Los metros cuadrados deben ser mayores a 0.';
      return;
    }
    if (this.form.capacidad <= 0) {
      this.errorGeneral = 'La capacidad debe ser mayor a 0.';
      return;
    }

    if (this.modoEdicion && this.idEdicion !== null) {
      this.tipoHabitacionService.update(this.idEdicion, this.form);
    } else {
      this.tipoHabitacionService.create(this.form);
    }

    this.router.navigate(['/admin/tipos-habitacion']);
  }

  /**
   * Cancela la edición y vuelve al listado administrativo.
   */
  cancelar(): void {
    this.router.navigate(['/admin/tipos-habitacion']);
  }
}
