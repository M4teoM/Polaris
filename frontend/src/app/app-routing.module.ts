import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { HabitacionesListaComponent } from './pages/habitaciones-lista/habitaciones-lista.component';
import { HabitacionDetalleComponent } from './pages/habitacion-detalle/habitacion-detalle.component';
import { ServiciosListaComponent } from './pages/servicios-lista/servicios-lista.component';
import { ServicioDetalleComponent } from './pages/servicio-detalle/servicio-detalle.component';
import { TipoHabitacionListaComponent } from './pages/tipo-habitacion-lista/tipo-habitacion-lista.component';
import { TipoHabitacionFormComponent } from './pages/tipo-habitacion-form/tipo-habitacion-form.component';
import { AdminServiciosListaComponent } from './pages/admin-servicios-lista/admin-servicios-lista.component';
import { AdminServicioFormComponent } from './pages/admin-servicio-form/admin-servicio-form.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'habitaciones', component: HabitacionesListaComponent },
  { path: 'habitaciones/:id', component: HabitacionDetalleComponent },
  { path: 'servicios', component: ServiciosListaComponent },
  { path: 'servicios/:id', component: ServicioDetalleComponent },
  { path: 'admin/servicios', component: AdminServiciosListaComponent },
  { path: 'admin/servicios/nuevo', component: AdminServicioFormComponent },
  { path: 'admin/servicios/editar/:id', component: AdminServicioFormComponent },
  { path: 'admin/tipos-habitacion', component: TipoHabitacionListaComponent },
  {
    path: 'admin/tipos-habitacion/nuevo',
    component: TipoHabitacionFormComponent,
  },
  {
    path: 'admin/tipos-habitacion/editar/:id',
    component: TipoHabitacionFormComponent,
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
