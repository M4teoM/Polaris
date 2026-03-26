import { Component, OnInit, OnDestroy } from '@angular/core';
import { Testimonio } from '../../models/testimonio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-testimonios',
  templateUrl: './testimonios.component.html',
  styleUrls: ['./testimonios.component.css'],
})
export class TestimoniosComponent implements OnInit, OnDestroy {
  testimonios: Testimonio[] = [];
  currentSlide = 0;
  private autoplayInterval: any;

  constructor(private servicioService: ServicioService) {}

  /**
   * Carga testimonios y arranca la reproducción automática del carrusel.
   */
  ngOnInit() {
    this.testimonios = this.servicioService.getTestimonios();
    this.startAutoplay();
  }

  /**
   * Detiene el autoplay al destruir el componente.
   */
  ngOnDestroy() {
    this.stopAutoplay();
  }

  /**
   * Inicia el avance automático de diapositivas.
   */
  startAutoplay() {
    this.autoplayInterval = setInterval(() => {
      this.nextSlide();
    }, 6000);
  }

  /**
   * Detiene el temporizador de autoplay si está activo.
   */
  stopAutoplay() {
    if (this.autoplayInterval) {
      clearInterval(this.autoplayInterval);
    }
  }

  /**
   * Avanza al siguiente testimonio en forma circular.
   */
  nextSlide() {
    this.currentSlide = (this.currentSlide + 1) % this.testimonios.length;
  }

  /**
   * Retrocede al testimonio anterior en forma circular.
   */
  prevSlide() {
    this.currentSlide =
      this.currentSlide === 0
        ? this.testimonios.length - 1
        : this.currentSlide - 1;
  }

  /**
   * Salta a una diapositiva específica y reinicia el autoplay.
   * @param index Índice de la diapositiva destino.
   */
  goToSlide(index: number) {
    this.currentSlide = index;
    this.stopAutoplay();
    this.startAutoplay();
  }

  /**
   * Genera un arreglo auxiliar para pintar estrellas en la vista.
   * @param count Cantidad de estrellas a renderizar.
   * @returns Arreglo con longitud igual al número de estrellas.
   */
  getStars(count: number): number[] {
    return Array(count).fill(0);
  }
}
