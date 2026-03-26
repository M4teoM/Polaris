import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search-card',
  templateUrl: './search-card.component.html',
  styleUrls: ['./search-card.component.css'],
})
export class SearchCardComponent implements OnInit {
  checkIn: string = '';
  checkOut: string = '';
  guests: string = '2';
  roomType: string = 'todas';
  minDate: string = '';

  /**
   * Inicializa fechas mínimas y valores por defecto del formulario de búsqueda.
   */
  ngOnInit() {
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0];
    this.checkIn = this.minDate;

    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.checkOut = tomorrow.toISOString().split('T')[0];
  }

  /**
   * Dispara la búsqueda de habitaciones con los filtros seleccionados.
   */
  onSearch() {
    console.log('Búsqueda:', {
      checkIn: this.checkIn,
      checkOut: this.checkOut,
      guests: this.guests,
      roomType: this.roomType,
    });
    // TODO: Implementar búsqueda real
    alert(
      `Buscando habitaciones del ${this.checkIn} al ${this.checkOut} para ${this.guests} huéspedes`,
    );
  }
}
