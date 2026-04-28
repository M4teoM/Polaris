import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeroComponent } from './components/hero/hero.component';
import { SearchCardComponent } from './components/search-card/search-card.component';
import { HabitacionesComponent } from './components/habitaciones/habitaciones.component';
import { ExploreComponent } from './components/explore/explore.component';
import { ServiciosComponent } from './components/servicios/servicios.component';
import { TestimoniosComponent } from './components/testimonios/testimonios.component';
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
import { AdminClientesListaComponent } from './pages/admin-clientes-lista/admin-clientes-lista.component';
import { AdminHabitacionesListaComponent } from './pages/admin-habitaciones-lista/admin-habitaciones-lista.component';
import { AdminPanelComponent } from './pages/admin-panel/admin-panel.component';
import { OperadorPanelComponent } from './pages/operador-panel/operador-panel.component';
import { ClientePerfilComponent } from './pages/cliente-perfil/cliente-perfil.component';
import { ReservasListaComponent } from './pages/reservas-lista/reservas-lista.component';
import { ReservaDetalleComponent } from './pages/reserva-detalle/reserva-detalle.component';
import { ReservaCrearComponent } from './pages/reserva-crear/reserva-crear.component';
import { OperadorContratarServicioComponent } from './pages/operador-contratar-servicio/operador-contratar-servicio.component';

import { PortalUsuarioComponent } from './pages/portal-usuario/portal-usuario.component';
import { UserSidebarComponent } from './components/user-sidebar/user-sidebar.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { ActiveReservationsComponent } from './components/active-reservations/active-reservations.component';
import { ReservationHistoryComponent } from './components/reservation-history/reservation-history.component';
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    HeroComponent,
    SearchCardComponent,
    HabitacionesComponent,
    ExploreComponent,
    ServiciosComponent,
    TestimoniosComponent,
    HomeComponent,
    LoginComponent,
    RegistroComponent,
    HabitacionesListaComponent,
    HabitacionDetalleComponent,
    ServiciosListaComponent,
    ServicioDetalleComponent,
    AdminServiciosListaComponent,
    AdminServicioFormComponent,
    AdminPanelComponent,
    OperadorPanelComponent,
    TipoHabitacionListaComponent,
    TipoHabitacionFormComponent,
    AdminClientesListaComponent,
    AdminHabitacionesListaComponent,
    ClientePerfilComponent,
    ReservasListaComponent,
    ReservaDetalleComponent,
    ReservaCrearComponent,
    OperadorContratarServicioComponent,
    PortalUsuarioComponent,
    UserSidebarComponent,
    UserProfileComponent,
    ActiveReservationsComponent,
    ReservationHistoryComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule, HttpClientModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
