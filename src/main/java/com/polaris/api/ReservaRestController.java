package com.polaris.api;

import com.polaris.model.ReservaHabitacion;
import com.polaris.service.IReservaHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de reservas de habitaciones.
 * Expone los endpoints bajo /api/reservas y utiliza ReservaResponse
 * para evitar devolver objetos anidados al frontend.
 */
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaRestController {

    @Autowired
    private IReservaHabitacionService reservaService;

    /**
     * DTO de respuesta plano. Incluye los campos de cliente y habitación
     * directamente, sin objetos anidados.
     */
    public record ReservaResponse(
            Long id,
            LocalDate fechaCheckIn,
            LocalDate fechaCheckOut,
            String estado,
            int numeroHuespedes,
            Long clienteId,
            String clienteNombre,
            String clienteApellido,
            String clienteCorreo,
            Long habitacionId,
            String habitacionNumero,
            Long tipoHabitacionId,
            String tipoHabitacionNombre
    ) {}

    /**
     * Convierte una entidad ReservaHabitacion en un DTO plano ReservaResponse.
     */
    private ReservaResponse toResponse(ReservaHabitacion r) {
        return new ReservaResponse(
                r.getId(),
                r.getFechaCheckIn(),
                r.getFechaCheckOut(),
                r.getEstado(),
                r.getNumeroHuespedes(),
                r.getCliente() != null ? r.getCliente().getId() : null,
                r.getCliente() != null ? r.getCliente().getNombre() : null,
                r.getCliente() != null ? r.getCliente().getApellido() : null,
                r.getCliente() != null ? r.getCliente().getCorreo() : null,
                r.getHabitacion() != null ? r.getHabitacion().getId() : null,
                r.getHabitacion() != null ? r.getHabitacion().getNumero() : null,
                r.getHabitacion() != null && r.getHabitacion().getTipoHabitacion() != null
                        ? r.getHabitacion().getTipoHabitacion().getId() : null,
                r.getHabitacion() != null && r.getHabitacion().getTipoHabitacion() != null
                        ? r.getHabitacion().getTipoHabitacion().getNombre() : null
        );
    }

    /** Retorna la lista completa de reservas en formato plano. */
    @GetMapping
    public List<ReservaResponse> listar() {
        return reservaService.obtenerTodos().stream()
                .map(this::toResponse)
                .toList();
    }

    /** Retorna una reserva específica por su ID en formato plano. */
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(reservaService.obtenerPorId(id)));
    }

    /** Retorna todas las reservas asociadas a un cliente específico. */
    @GetMapping("/cliente/{clienteId}")
    public List<ReservaResponse> porCliente(@PathVariable Long clienteId) {
        return reservaService.obtenerPorCliente(clienteId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Crea una nueva reserva.
     * Angular manda: { clienteId, tipoHabitacionId, fechaCheckIn, fechaCheckOut, numeroHuespedes }
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long clienteId    = Long.valueOf(body.get("clienteId").toString());
            Long tipoId       = Long.valueOf(body.get("tipoHabitacionId").toString());
            LocalDate checkIn = LocalDate.parse(body.get("fechaCheckIn").toString());
            LocalDate checkOut= LocalDate.parse(body.get("fechaCheckOut").toString());
            int huespedes     = Integer.parseInt(body.get("numeroHuespedes").toString());

            reservaService.crearDesdeDetalle(clienteId, tipoId, checkIn, checkOut, huespedes);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva creada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Actualiza fechas, número de huéspedes y/o estado de una reserva existente. */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Map<String, Object> body) {
        try {
            ReservaHabitacion reserva = reservaService.obtenerPorId(id);

            LocalDate checkIn = body.containsKey("fechaCheckIn")
                    ? LocalDate.parse(body.get("fechaCheckIn").toString())
                    : reserva.getFechaCheckIn();
            LocalDate checkOut = body.containsKey("fechaCheckOut")
                    ? LocalDate.parse(body.get("fechaCheckOut").toString())
                    : reserva.getFechaCheckOut();

            if (!checkOut.isAfter(checkIn)) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "La fecha de salida debe ser posterior a la de entrada."));
            }

            reserva.setFechaCheckIn(checkIn);
            reserva.setFechaCheckOut(checkOut);

            if (body.containsKey("numeroHuespedes")) {
                reserva.setNumeroHuespedes(Integer.parseInt(body.get("numeroHuespedes").toString()));
            }

            if (body.containsKey("estado") && body.get("estado") != null) {
                String estado = body.get("estado").toString().trim();
                if (!estado.isBlank()) reserva.setEstado(estado);
            }

            reservaService.actualizar(reserva);
            return ResponseEntity.ok(toResponse(reservaService.obtenerPorId(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Cambia el estado de una reserva a "Cancelada".
     * Solo puede hacerlo el cliente dueño de la reserva.
     */
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id,
                                      @RequestBody Map<String, Object> body) {
        try {
            Long clienteId = Long.valueOf(body.get("clienteId").toString());
            reservaService.cancelar(id, clienteId);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Elimina una reserva por su ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}