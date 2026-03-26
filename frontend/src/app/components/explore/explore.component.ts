import {
  Component,
  OnInit,
  ElementRef,
  ViewChild,
  HostListener,
} from '@angular/core';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.css'],
})
export class ExploreComponent implements OnInit {
  isInView = false;
  @ViewChild('exploreCard', { static: true }) exploreCard!: ElementRef;

  /**
   * Ejecuta la validación inicial de visibilidad al montar el componente.
   */
  ngOnInit(): void {
    this.checkIfInView();
  }

  @HostListener('window:scroll')
  /**
   * Recalcula si la tarjeta está en viewport cuando ocurre scroll.
   */
  onScroll(): void {
    this.checkIfInView();
  }

  /**
   * Evalúa la posición del bloque para activar animaciones al entrar en vista.
   */
  private checkIfInView(): void {
    if (this.exploreCard) {
      const rect = this.exploreCard.nativeElement.getBoundingClientRect();
      const windowHeight = window.innerHeight;
      this.isInView = rect.top < windowHeight * 0.85;
    }
  }
}
