package com.polaris.service;

import com.polaris.errors.ErrorReservaException;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.Cliente;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaHabitacionServiceTest {

	// Servicio real que se prueba, con sus dependencias inyectadas por Mockito.
    @InjectMocks
    private ReservaHabitacionService service;

	// Repositorios simulados para controlar el comportamiento del servicio.
    @Mock
    private IReservaHabitacionRepository repository;

    @Mock
    private IClienteRepository clienteRepository;

    @Mock
    private IHabitacionRepository habitacionRepository;

    @Test
    void crearDesdeDetalle_CasoExito_guardaReservaConDatosCorrectos() {
		// Arrange: se preparan los datos de entrada y los mocks necesarios.
	Long clienteId = 1L;
	Long tipoHabitacionId = 10L;
	LocalDate checkIn = LocalDate.now().plusDays(5);
	LocalDate checkOut = LocalDate.now().plusDays(8);
	int numeroHuespedes = 2;

	Cliente cliente = Cliente.builder()
		.id(clienteId)
		.nombre("Juan")
		.apellido("Perez")
		.correo("juan.perez@example.com")
		.contrasena("secret")
		.build();

	TipoHabitacion tipo = TipoHabitacion.builder()
		.id(tipoHabitacionId)
		.capacidad(3)
		.nombre("Doble")
		.descripcion("Doble confortable")
		.precioPorNoche(100.0)
		.metrosCuadrados(20)
		.tipoCama("Queen")
		.build();

	Habitacion habitacion = Habitacion.builder()
		.id(100L)
		.numero("101A")
		.piso(1)
		.estado("disponible")
		.tipoHabitacion(tipo)
		.build();

	when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
	when(repository.findHabitacionesOcupadasEnRango(checkIn, checkOut))
		.thenReturn(Collections.emptyList());
	when(habitacionRepository.findByTipoHabitacion_Id(tipoHabitacionId))
		.thenReturn(List.of(habitacion));

	when(repository.save(any(ReservaHabitacion.class)))
		.thenAnswer(invocation -> invocation.getArgument(0));

	// Act: se ejecuta la creación de la reserva.
	service.crearDesdeDetalle(clienteId, tipoHabitacionId, checkIn, checkOut, numeroHuespedes);

	// Assert: se verifica que la reserva guardada tenga los valores esperados.
	ArgumentCaptor<ReservaHabitacion> captor = ArgumentCaptor.forClass(ReservaHabitacion.class);
	verify(repository, times(1)).save(captor.capture());
	ReservaHabitacion guardada = captor.getValue();

	assertThat(guardada).isNotNull();
	assertThat(guardada.getCliente()).isNotNull();
	assertThat(guardada.getCliente().getId()).isEqualTo(clienteId);
	assertThat(guardada.getHabitacion()).isNotNull();
	assertThat(guardada.getHabitacion().getId()).isEqualTo(habitacion.getId());
	assertThat(guardada.getNumeroHuespedes()).isEqualTo(numeroHuespedes);
	assertThat(guardada.getFechaCheckIn()).isEqualTo(checkIn);
	assertThat(guardada.getFechaCheckOut()).isEqualTo(checkOut);
    }

    @Test
    void crearDesdeDetalle_NoHayDisponibilidad_lanzaErrorReservaException() {
		// Arrange: se simula que no existe ninguna habitación disponible del tipo solicitado.
	Long clienteId = 2L;
	Long tipoHabitacionId = 20L;
	LocalDate checkIn = LocalDate.now().plusDays(2);
	LocalDate checkOut = LocalDate.now().plusDays(4);
	int numeroHuespedes = 1;

	Cliente cliente = Cliente.builder()
		.id(clienteId)
		.nombre("Ana")
		.apellido("Gomez")
		.correo("ana.gomez@example.com")
		.contrasena("secret")
		.build();

	when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
	when(repository.findHabitacionesOcupadasEnRango(checkIn, checkOut))
		.thenReturn(Collections.emptyList());
	when(habitacionRepository.findByTipoHabitacion_Id(tipoHabitacionId))
		.thenReturn(Collections.emptyList());

	// Act & Assert: la regla de negocio debe impedir la reserva y lanzar la excepción.
	assertThatThrownBy(() -> service.crearDesdeDetalle(clienteId, tipoHabitacionId, checkIn, checkOut, numeroHuespedes))
		.isInstanceOf(ErrorReservaException.class)
		.hasMessageContaining("No hay habitaciones disponibles");
    }
}

