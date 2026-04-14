import { Component, OnInit } from '@angular/core';
import { Servicio } from '../../models/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-servicios-lista',
  templateUrl: './servicios-lista.component.html',
  styleUrls: ['./servicios-lista.component.css'],
})
export class ServiciosListaComponent implements OnInit {
  servicios: Servicio[] = [];
  serviciosFiltrados: Servicio[] = [];
  filtroActivo = 'all';
  mostrarCrud = false;
  editandoId: number | null = null;

  nuevoServicio: Omit<Servicio, 'id'> = this.crearServicioVacio();

  constructor(private servicioService: ServicioService) {}

  /**
   * Inicializa lista completa y lista filtrada de servicios.
   */
  ngOnInit(): void {
    this.recargarServicios();
  }

  /**
   * Aplica filtro por categoría visual para actualizar el listado visible.
   * @param filtro Clave del filtro activo.
   */
  filtrar(filtro: string): void {
    this.filtroActivo = filtro;
    if (filtro === 'all') {
      this.serviciosFiltrados = this.servicios;
    } else {
      this.serviciosFiltrados = this.servicios.filter(
        (s) => s.icono === filtro,
      );
    }
  }

  crearServicio(): void {
    if (!this.nuevoServicio.nombre.trim() || !this.nuevoServicio.categoria.trim()) {
      return;
    }

    this.servicioService.createServicio({
      ...this.nuevoServicio,
      nombre: this.nuevoServicio.nombre.trim(),
      categoria: this.nuevoServicio.categoria.trim(),
      descripcion: this.nuevoServicio.descripcion.trim(),
      descripcionDetallada: this.nuevoServicio.descripcionDetallada?.trim(),
      imagenUrl: this.nuevoServicio.imagenUrl.trim(),
    });

    this.nuevoServicio = this.crearServicioVacio();
    this.recargarServicios();
  }

  editarServicio(servicio: Servicio): void {
    this.mostrarCrud = true;
    this.editandoId = servicio.id;
    this.nuevoServicio = {
      nombre: servicio.nombre,
      descripcion: servicio.descripcion,
      descripcionDetallada: servicio.descripcionDetallada,
      precio: servicio.precio,
      imagenUrl: servicio.imagenUrl,
      categoria: servicio.categoria,
      duracion: servicio.duracion,
      horario: servicio.horario,
      incluye: servicio.incluye,
      destacados: servicio.destacados,
      icono: servicio.icono,
    };
  }

  guardarEdicion(): void {
    if (this.editandoId == null) {
      return;
    }

    this.servicioService.updateServicio({
      id: this.editandoId,
      ...this.nuevoServicio,
      nombre: this.nuevoServicio.nombre.trim(),
      categoria: this.nuevoServicio.categoria.trim(),
      descripcion: this.nuevoServicio.descripcion.trim(),
      descripcionDetallada: this.nuevoServicio.descripcionDetallada?.trim(),
      imagenUrl: this.nuevoServicio.imagenUrl.trim(),
    });

    this.cancelarEdicion();
    this.recargarServicios();
  }

  cancelarEdicion(): void {
    this.editandoId = null;
    this.nuevoServicio = this.crearServicioVacio();
  }

  toggleCrud(): void {
    this.mostrarCrud = !this.mostrarCrud;
    if (!this.mostrarCrud) {
      this.cancelarEdicion();
    }
  }

  eliminarServicio(id: number): void {
    this.servicioService.deleteServicio(id);
    this.recargarServicios();
  }

  private recargarServicios(): void {
    this.servicios = this.servicioService.getServicios();
    this.filtrar(this.filtroActivo);
  }

  private crearServicioVacio(): Omit<Servicio, 'id'> {
    return {
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
      icono: 'star',
    };
  }
}
