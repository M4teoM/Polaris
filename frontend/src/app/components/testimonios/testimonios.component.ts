import { Component, OnInit, OnDestroy } from '@angular/core';
import { Testimonio } from '../../models/testimonio';

@Component({
  selector: 'app-testimonios',
  templateUrl: './testimonios.component.html',
  styleUrls: ['./testimonios.component.css'],
})
export class TestimoniosComponent implements OnInit, OnDestroy {
  testimonios: Testimonio[] = [
    {
      id: 1,
      texto: 'Una experiencia inigualable. El servicio es impecable y las instalaciones superaron todas mis expectativas.',
      autorNombre: 'María González',
      autorUbicacion: 'Bogotá, Colombia',
      estrellas: 5,
    },
    {
      id: 2,
      texto: 'El mejor hotel en el que he estado. La atención personalizada y la calidad de las habitaciones son excepcionales.',
      autorNombre: 'Carlos Rodríguez',
      autorUbicacion: 'Medellín, Colombia',
      estrellas: 5,
    },
    {
      id: 3,
      texto: 'Perfecta combinación de lujo y comodidad. El spa es simplemente divino. Volveré sin duda.',
      autorNombre: 'Ana Martínez',
      autorUbicacion: 'Cali, Colombia',
      estrellas: 5,
    },
  ];

  currentSlide = 0;
  private autoplayInterval: any;

  ngOnInit() {
    this.startAutoplay();
  }

  ngOnDestroy() {
    this.stopAutoplay();
  }

  startAutoplay() {
    this.autoplayInterval = setInterval(() => this.nextSlide(), 6000);
  }

  stopAutoplay() {
    if (this.autoplayInterval) clearInterval(this.autoplayInterval);
  }

  nextSlide() {
    this.currentSlide = (this.currentSlide + 1) % this.testimonios.length;
  }

  prevSlide() {
    this.currentSlide = this.currentSlide === 0
      ? this.testimonios.length - 1
      : this.currentSlide - 1;
  }

  goToSlide(index: number) {
    this.currentSlide = index;
    this.stopAutoplay();
    this.startAutoplay();
  }

  getStars(count: number): number[] {
    return Array(count).fill(0);
  }
}