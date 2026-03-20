import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { HabitacionesListaComponent } from './pages/habitaciones-lista/habitaciones-lista.component';
import { HabitacionDetalleComponent } from './pages/habitacion-detalle/habitacion-detalle.component';
import { ServiciosListaComponent } from './pages/servicios-lista/servicios-lista.component';
import { ServicioDetalleComponent } from './pages/servicio-detalle/servicio-detalle.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'habitaciones', component: HabitacionesListaComponent },
  { path: 'habitaciones/:id', component: HabitacionDetalleComponent },
  { path: 'servicios', component: ServiciosListaComponent },
  { path: 'servicios/:id', component: ServicioDetalleComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { 
    scrollPositionRestoration: 'enabled',
    anchorScrolling: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
