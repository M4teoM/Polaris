package com.polaris.service;

import com.polaris.errors.ErrorReservaException;
import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.ICuentaRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaHabitacionServiceTest {

    @Mock
    private IReservaHabitacionRepository repository;
    @Mock
    private IClienteRepository clienteRepository;
    @Mock
    private IHabitacionRepository habitacionRepository;
    @Mock
    private ICuentaRepository cuentaRepository;

    @InjectMocks
    private ReservaHabitacionService reservaHabitacionService;

    @Test
    void crearDesdeDetalle_debeGuardarReservaInactivaSiHayDisponibilidad() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        TipoHabitacion tipo = new TipoHabitacion();
        tipo.setId(2L);
        tipo.setCapacidad(3);

        Habitacion habitacion = new Habitacion();
        habitacion.setId(10L);
        habitacion.setEstado("Disponible");
        habitacion.setTipoHabitacion(tipo);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(repository.findHabitacionesOcupadasEnRango(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());
        when(habitacionRepository.findByTipoHabitacion_Id(2L)).thenReturn(List.of(habitacion));

        reservaHabitacionService.crearDesdeDetalle(
                1L, 2L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 2);

        ArgumentCaptor<ReservaHabitacion> captor = ArgumentCaptor.forClass(ReservaHabitacion.class);
        verify(repository).save(captor.capture());
        assertEquals("Inactiva", captor.getValue().getEstado());
        assertEquals(2, captor.getValue().getNumeroHuespedes());
    }

    @Test
    void crearDesdeDetalle_debeFallarSiNoHayHabitacionesDisponibles() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        TipoHabitacion tipo = new TipoHabitacion();
        tipo.setId(2L);
        tipo.setCapacidad(2);

        Habitacion ocupada = new Habitacion();
        ocupada.setId(99L);
        ocupada.setEstado("Disponible");
        ocupada.setTipoHabitacion(tipo);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(repository.findHabitacionesOcupadasEnRango(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(99L));
        when(habitacionRepository.findByTipoHabitacion_Id(2L)).thenReturn(List.of(ocupada));

        assertThrows(
                ErrorReservaException.class,
                () -> reservaHabitacionService.crearDesdeDetalle(
                        1L, 2L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 2));

        verify(repository, never()).save(any(ReservaHabitacion.class));
    }

    @Test
    void cancelar_debeCambiarEstadoACanceladaSiClienteEsDueno() {
        Cliente cliente = new Cliente();
        cliente.setId(5L);

        ReservaHabitacion reserva = new ReservaHabitacion();
        reserva.setId(20L);
        reserva.setCliente(cliente);
        reserva.setEstado("Inactiva");

        when(repository.findById(20L)).thenReturn(Optional.of(reserva));

        reservaHabitacionService.cancelar(20L, 5L);

        ArgumentCaptor<ReservaHabitacion> captor = ArgumentCaptor.forClass(ReservaHabitacion.class);
        verify(repository).save(captor.capture());
        assertEquals("Cancelada", captor.getValue().getEstado());
    }

    @Test
    void cancelar_debeFallarSiClienteNoEsDueno() {
        Cliente cliente = new Cliente();
        cliente.setId(5L);

        ReservaHabitacion reserva = new ReservaHabitacion();
        reserva.setId(30L);
        reserva.setCliente(cliente);

        when(repository.findById(30L)).thenReturn(Optional.of(reserva));

        assertThrows(ErrorReservaException.class, () -> reservaHabitacionService.cancelar(30L, 99L));
        verify(repository, never()).save(any(ReservaHabitacion.class));
    }
}
