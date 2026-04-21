package com.polaris.api;

import com.polaris.model.ReservaHabitacion;
import com.polaris.service.IReservaHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaRestController {

    @Autowired
    private IReservaHabitacionService reservaService;

    @GetMapping
    public List<ReservaHabitacion> listar() {
        return reservaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaHabitacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public List<ReservaHabitacion> porCliente(@PathVariable Long clienteId) {
        return reservaService.obtenerPorCliente(clienteId);
    }

    // Angular manda: { clienteId, tipoHabitacionId, fechaCheckIn, fechaCheckOut, numeroHuespedes }
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long clienteId       = Long.valueOf(body.get("clienteId").toString());
            Long tipoId          = Long.valueOf(body.get("tipoHabitacionId").toString());
            LocalDate checkIn    = LocalDate.parse(body.get("fechaCheckIn").toString());
            LocalDate checkOut   = LocalDate.parse(body.get("fechaCheckOut").toString());
            int huespedes        = Integer.parseInt(body.get("numeroHuespedes").toString());

            reservaService.crearDesdeDetalle(clienteId, tipoId, checkIn, checkOut, huespedes);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva creada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

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
                if (!estado.isBlank()) {
                    reserva.setEstado(estado);
                }
            }

            reservaService.actualizar(reserva);
            return ResponseEntity.ok(reservaService.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}