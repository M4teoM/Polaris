import { Component, OnInit, OnDestroy } from '@angular/core';
import { Testimonio } from '../../models/testimonio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-testimonios',
  templateUrl: './testimonios.component.html',
  styleUrls: ['./testimonios.component.css']
})
export class TestimoniosComponent implements OnInit, OnDestroy {
  testimonios: Testimonio[] = [];
  currentSlide = 0;
  private autoplayInterval: any;

  constructor(private servicioService: ServicioService) {}

  ngOnInit() {
    this.testimonios = this.servicioService.getTestimonios();
    this.startAutoplay();
  }

  ngOnDestroy() {
    this.stopAutoplay();
  }

  startAutoplay() {
    this.autoplayInterval = setInterval(() => {
      this.nextSlide();
    }, 6000);
  }

  stopAutoplay() {
    if (this.autoplayInterval) {
      clearInterval(this.autoplayInterval);
    }
  }

  nextSlide() {
    this.currentSlide = (this.currentSlide + 1) % this.testimonios.length;
  }

  prevSlide() {
    this.currentSlide = this.currentSlide === 0 ? this.testimonios.length - 1 : this.currentSlide - 1;
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
