import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css'],
})
export class AdminPanelComponent implements OnInit, OnDestroy {
  seccionActual = 'CRUD Servicios';
  private routerSub?: Subscription;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.actualizarSeccionActual();
    this.routerSub = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => this.actualizarSeccionActual());
  }

  ngOnDestroy(): void {
    this.routerSub?.unsubscribe();
  }

  private actualizarSeccionActual(): void {
    let current = this.route.firstChild;

    while (current?.firstChild) {
      current = current.firstChild;
    }

    this.seccionActual =
      (current?.snapshot.data?.['sectionLabel'] as string) ||
      'Panel de Administración';
  }
}
