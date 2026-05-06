package com.polaris.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.service.IReservaHabitacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaRestController.class)
class ReservaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IReservaHabitacionService reservaService;

    @Test
    void listar_debeRetornarListaConTamanioEsperado() throws Exception {
	// Arrange: se prepara una única reserva para que el controlador la serialice.
	ReservaHabitacion reserva = crearReserva(1L, "Inactiva", 2);
	when(reservaService.obtenerTodos()).thenReturn(List.of(reserva));

	// Act: se invoca el endpoint GET de listado.
	mockMvc.perform(get("/api/reservas"))
		// Assert: la respuesta debe contener un array con un elemento y sus campos principales.
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].id").value(1L))
		.andExpect(jsonPath("$[0].clienteNombre").value("Juan"))
		.andExpect(jsonPath("$[0].habitacionNumero").value("101"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarOkYJsonCorrecto() throws Exception {
	// Arrange: se simula que el servicio devuelve una reserva válida.
	ReservaHabitacion reserva = crearReserva(2L, "Confirmada", 3);
	when(reservaService.obtenerPorId(2L)).thenReturn(reserva);

	// Act: se consulta la reserva por su ID.
	mockMvc.perform(get("/api/reservas/{id}", 2L))
		// Assert: el status es 200 y el JSON contiene los datos esperados.
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(2L))
		.andExpect(jsonPath("$.estado").value("Confirmada"))
		.andExpect(jsonPath("$.clienteCorreo").value("juan@example.com"));
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarNotFound() throws Exception {
	// Arrange: el servicio responde null para simular que no existe la reserva.
	when(reservaService.obtenerPorId(99L)).thenReturn(null);

	// Act: se intenta consultar un ID inexistente.
	mockMvc.perform(get("/api/reservas/{id}", 99L))
		// Assert: el controlador responde con 404.
		.andExpect(status().isNotFound());
    }

    @Test
    void crear_debeRetornarCreated() throws Exception {
	// Arrange: se arma el body de creación y se convierte a JSON con ObjectMapper.
	Map<String, Object> body = Map.of(
		"clienteId", 1L,
		"tipoHabitacionId", 10L,
		"fechaCheckIn", LocalDate.now().plusDays(5).toString(),
		"fechaCheckOut", LocalDate.now().plusDays(8).toString(),
		"numeroHuespedes", 2
	);
	String json = objectMapper.writeValueAsString(body);

	// Act: se envía la petición POST al endpoint de creación.
	mockMvc.perform(post("/api/reservas")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		// Assert: el controlador debe responder 201 Created.
		.andExpect(status().isCreated());

	verify(reservaService, times(1)).crearDesdeDetalle(
		eq(1L), eq(10L), any(LocalDate.class), any(LocalDate.class), eq(2));
    }

    @Test
    void actualizar_debeRetornarOk() throws Exception {
	// Arrange: se prepara la reserva actual y el body con los campos actualizados.
	ReservaHabitacion reservaActual = crearReserva(5L, "Inactiva", 2);
	ReservaHabitacion reservaActualizada = crearReserva(5L, "Confirmada", 4);
	Map<String, Object> body = Map.of(
		"fechaCheckIn", LocalDate.now().plusDays(6).toString(),
		"fechaCheckOut", LocalDate.now().plusDays(9).toString(),
		"numeroHuespedes", 4,
		"estado", "Confirmada"
	);
	String json = objectMapper.writeValueAsString(body);

	when(reservaService.obtenerPorId(5L)).thenReturn(reservaActual, reservaActualizada);

	// Act: se envía la petición PUT con los datos modificados.
	mockMvc.perform(put("/api/reservas/{id}", 5L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		// Assert: la actualización debe responder correctamente.
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(5L))
		.andExpect(jsonPath("$.estado").value("Confirmada"))
		.andExpect(jsonPath("$.numeroHuespedes").value(4));

	verify(reservaService, times(1)).actualizar(any(ReservaHabitacion.class));
    }

    @Test
    void eliminar_debeRetornarNoContent() throws Exception {
	// Arrange: no se necesita respuesta del servicio para eliminar.

	// Act: se ejecuta el DELETE sobre la reserva indicada.
	mockMvc.perform(delete("/api/reservas/{id}", 7L))
		// Assert: el controlador responde 204 No Content.
		.andExpect(status().isNoContent());

	verify(reservaService, times(1)).eliminar(7L);
    }

    // Construye una reserva completa para reutilizarla en los distintos escenarios.
    private ReservaHabitacion crearReserva(Long id, String estado, int numeroHuespedes) {
	TipoHabitacion tipoHabitacion = TipoHabitacion.builder()
		.id(10L)
		.nombre("Doble")
		.descripcion("Habitación doble")
		.precioPorNoche(120.0)
		.metrosCuadrados(25)
		.capacidad(4)
		.tipoCama("Queen")
		.build();

	Habitacion habitacion = Habitacion.builder()
		.id(100L)
		.numero("101")
		.piso(1)
		.estado("disponible")
		.tipoHabitacion(tipoHabitacion)
		.build();

	Cliente cliente = Cliente.builder()
		.id(1L)
		.nombre("Juan")
		.apellido("Pérez")
		.correo("juan@example.com")
		.contrasena("secret")
		.build();

	return ReservaHabitacion.builder()
		.id(id)
		.fechaCheckIn(LocalDate.now().plusDays(5))
		.fechaCheckOut(LocalDate.now().plusDays(8))
		.estado(estado)
		.numeroHuespedes(numeroHuespedes)
		.cliente(cliente)
		.habitacion(habitacion)
		.build();
    }
}
