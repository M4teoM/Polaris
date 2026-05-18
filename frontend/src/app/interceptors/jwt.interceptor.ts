import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  // Este método se ejecuta automáticamente en CADA petición HTTP que hace Angular
  // Si hay un token guardado, lo adjunta al header Authorization
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    if (token) {
      // Clonamos la request (las requests son inmutables) y le agregamos el header
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(cloned);
    }

    // Si no hay token, dejamos pasar la request sin modificar
    return next.handle(req);
  }
}