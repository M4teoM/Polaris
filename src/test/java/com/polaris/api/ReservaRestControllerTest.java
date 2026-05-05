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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservaRestController.class)
class ReservaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IReservaHabitacionService reservaService;

    @Test
    void listar_debeRetornarReservasEnFormatoPlano() throws Exception {
        when(reservaService.obtenerTodos()).thenReturn(List.of(buildReserva(1L)));

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteNombre").value("Juan"))
                .andExpect(jsonPath("$[0].habitacionNumero").value("101"));
    }

    @Test
    void obtenerPorId_debeRetornarReserva() throws Exception {
        when(reservaService.obtenerPorId(1L)).thenReturn(buildReserva(1L));

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipoHabitacionNombre").value("Suite"));
    }

    @Test
    void obtenerPorCliente_debeRetornarReservasDelCliente() throws Exception {
        when(reservaService.obtenerPorCliente(10L)).thenReturn(List.of(buildReserva(7L)));

        mockMvc.perform(get("/api/reservas/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7));
    }

    @Test
    void crear_debeDelegarEnServicio() throws Exception {
        Map<String, Object> body = Map.of(
                "clienteId", 10,
                "tipoHabitacionId", 2,
                "fechaCheckIn", "2026-05-10",
                "fechaCheckOut", "2026-05-12",
                "numeroHuespedes", 2
        );

        doNothing().when(reservaService).crearDesdeDetalle(
                eq(10L), eq(2L), any(LocalDate.class), any(LocalDate.class), eq(2));

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Reserva creada correctamente"));
    }

    @Test
    void actualizar_debeRetornarBadRequestSiFechasInvalidas() throws Exception {
        ReservaHabitacion existente = buildReserva(50L);
        when(reservaService.obtenerPorId(50L)).thenReturn(existente);

        Map<String, Object> body = Map.of(
                "fechaCheckIn", "2026-05-12",
                "fechaCheckOut", "2026-05-10"
        );

        mockMvc.perform(put("/api/reservas/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La fecha de salida debe ser posterior a la de entrada."));
    }

    @Test
    void eliminar_debeRetornarNoContent() throws Exception {
        doNothing().when(reservaService).eliminar(33L);

        mockMvc.perform(delete("/api/reservas/33"))
                .andExpect(status().isNoContent());

        verify(reservaService).eliminar(33L);
    }

    private ReservaHabitacion buildReserva(Long id) {
        Cliente cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setCorreo("juan@correo.com");

        TipoHabitacion tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setId(2L);
        tipoHabitacion.setNombre("Suite");

        Habitacion habitacion = new Habitacion();
        habitacion.setId(3L);
        habitacion.setNumero("101");
        habitacion.setTipoHabitacion(tipoHabitacion);

        ReservaHabitacion reserva = new ReservaHabitacion();
        reserva.setId(id);
        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);
        reserva.setEstado("Inactiva");
        reserva.setNumeroHuespedes(2);
        reserva.setFechaCheckIn(LocalDate.of(2026, 5, 10));
        reserva.setFechaCheckOut(LocalDate.of(2026, 5, 12));
        return reserva;
    }
}
