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

  ngOnInit(): void {
    this.recargarServicios();
  }

  filtrar(filtro: string): void {
    this.filtroActivo = filtro;
    this.serviciosFiltrados = filtro === 'all'
      ? this.servicios
      : this.servicios.filter(s => s.icono === filtro);
  }

  crearServicio(): void {
    if (!this.nuevoServicio.nombre.trim() || !this.nuevoServicio.categoria.trim()) return;
    this.servicioService.createServicio(this.nuevoServicio).subscribe(() => {
      this.nuevoServicio = this.crearServicioVacio();
      this.recargarServicios();
    });
  }

  editarServicio(servicio: Servicio): void {
    this.mostrarCrud = true;
    this.editandoId = servicio.id;
    this.nuevoServicio = { ...servicio };
  }

  guardarEdicion(): void {
    if (this.editandoId == null) return;
    this.servicioService.updateServicio({ id: this.editandoId, ...this.nuevoServicio }).subscribe(() => {
      this.cancelarEdicion();
      this.recargarServicios();
    });
  }

  cancelarEdicion(): void {
    this.editandoId = null;
    this.nuevoServicio = this.crearServicioVacio();
  }

  toggleCrud(): void {
    this.mostrarCrud = !this.mostrarCrud;
    if (!this.mostrarCrud) this.cancelarEdicion();
  }

  eliminarServicio(id: number): void {
    this.servicioService.deleteServicio(id).subscribe(() => {
      this.recargarServicios();
    });
  }

  private recargarServicios(): void {
    this.servicioService.getServicios().subscribe(data => {
      this.servicios = data;
      this.filtrar(this.filtroActivo);
    });
  }

  private crearServicioVacio(): Omit<Servicio, 'id'> {
    return { nombre: '', descripcion: '', descripcionDetallada: '', precio: 0,
      imagenUrl: '', categoria: '', duracion: '', horario: '', incluye: '', destacados: '', icono: 'star' };
  }
}