import { Component, OnInit, ElementRef, ViewChild, HostListener } from '@angular/core';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.css']
})
export class ExploreComponent implements OnInit {
  isInView = false;
  @ViewChild('exploreCard', { static: true }) exploreCard!: ElementRef;

  ngOnInit(): void {
    this.checkIfInView();
  }

  @HostListener('window:scroll')
  onScroll(): void {
    this.checkIfInView();
  }

  private checkIfInView(): void {
    if (this.exploreCard) {
      const rect = this.exploreCard.nativeElement.getBoundingClientRect();
      const windowHeight = window.innerHeight;
      this.isInView = rect.top < windowHeight * 0.85;
    }
  }
}
