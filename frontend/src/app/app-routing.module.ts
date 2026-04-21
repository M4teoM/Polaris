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
import { AdminGuard } from './guards/admin.guard';
import { OperadorGuard } from './guards/operador.guard';
import { AdminClientesListaComponent } from './pages/admin-clientes-lista/admin-clientes-lista.component';
import { AdminHabitacionesListaComponent } from './pages/admin-habitaciones-lista/admin-habitaciones-lista.component';
import { AdminPanelComponent } from './pages/admin-panel/admin-panel.component';
import { OperadorPanelComponent } from './pages/operador-panel/operador-panel.component';
import { ClientePerfilComponent } from './pages/cliente-perfil/cliente-perfil.component';
import { ReservasListaComponent } from './pages/reservas-lista/reservas-lista.component';
import { ReservaDetalleComponent } from './pages/reserva-detalle/reserva-detalle.component';
import { ReservaCrearComponent } from './pages/reserva-crear/reserva-crear.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'habitaciones', component: HabitacionesListaComponent },
  { path: 'habitaciones/:id', component: HabitacionDetalleComponent },
  { path: 'servicios', component: ServiciosListaComponent },
  { path: 'servicios/:id', component: ServicioDetalleComponent },
  { path: 'reservas', redirectTo: 'admin/reservas', pathMatch: 'full' },
  { path: 'reservas/nueva', component: ReservaCrearComponent },
  { path: 'reservas/:id', component: ReservaDetalleComponent },
  { path: 'clientes/ver', component: ClientePerfilComponent },
  {
    path: 'admin',
    component: AdminPanelComponent,
    canActivate: [AdminGuard],
    children: [
      { path: '', redirectTo: 'servicios', pathMatch: 'full' },
      {
        path: 'servicios',
        component: AdminServiciosListaComponent,
        data: { sectionLabel: 'CRUD Servicios' },
      },
      {
        path: 'servicios/nuevo',
        component: AdminServicioFormComponent,
        data: { sectionLabel: 'CRUD Servicios · Nuevo' },
      },
      {
        path: 'servicios/editar/:id',
        component: AdminServicioFormComponent,
        data: { sectionLabel: 'CRUD Servicios · Editar' },
      },
      {
        path: 'tipos-habitacion',
        component: TipoHabitacionListaComponent,
        data: { sectionLabel: 'CRUD Tipos de Habitación' },
      },
      {
        path: 'tipos-habitacion/nuevo',
        component: TipoHabitacionFormComponent,
        data: { sectionLabel: 'CRUD Tipos · Nuevo' },
      },
      {
        path: 'tipos-habitacion/editar/:id',
        component: TipoHabitacionFormComponent,
        data: { sectionLabel: 'CRUD Tipos · Editar' },
      },
      {
        path: 'clientes',
        component: AdminClientesListaComponent,
        data: { sectionLabel: 'CRUD Clientes' },
      },
      {
        path: 'habitaciones',
        component: AdminHabitacionesListaComponent,
        data: { sectionLabel: 'CRUD Habitaciones' },
      },
      {
        path: 'reservas',
        component: ReservasListaComponent,
        data: { sectionLabel: 'CRUD Reservas' },
      },
      {
        path: 'reservas/nueva',
        component: ReservaCrearComponent,
        data: { sectionLabel: 'CRUD Reservas · Nuevo' },
      },
      {
        path: 'reservas/editar/:id',
        component: ReservaCrearComponent,
        data: { sectionLabel: 'CRUD Reservas · Editar' },
      },
      {
        path: 'reservas/:id',
        component: ReservaDetalleComponent,
        data: { sectionLabel: 'CRUD Reservas · Detalle' },
      },
    ],
  },
  {
    path: 'operador',
    component: OperadorPanelComponent,
    canActivate: [OperadorGuard],
    children: [
      { path: '', redirectTo: 'reservas', pathMatch: 'full' },
      {
        path: 'reservas',
        component: ReservasListaComponent,
        data: {
          sectionLabel: 'Reservas',
          routePrefix: '/operador/reservas',
        },
      },
      {
        path: 'reservas/nueva',
        component: ReservaCrearComponent,
        data: {
          sectionLabel: 'Reservas · Nuevo',
          returnUrl: '/operador/reservas',
        },
      },
      {
        path: 'reservas/editar/:id',
        component: ReservaCrearComponent,
        data: {
          sectionLabel: 'Reservas · Editar',
          returnUrl: '/operador/reservas',
        },
      },
      {
        path: 'reservas/:id',
        component: ReservaDetalleComponent,
        data: { sectionLabel: 'Reservas · Detalle' },
      },
    ],
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
