import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class OperadorGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    // isOperario() e isOperador() son equivalentes — ambos existen en AuthService
    if (this.authService.isOperario()) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}