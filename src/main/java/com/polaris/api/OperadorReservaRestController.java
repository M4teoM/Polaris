package com.polaris.api;

import com.polaris.service.IReservaHabitacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operador/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class OperadorReservaRestController {

    private final IReservaHabitacionService reservaService;

    public OperadorReservaRestController(IReservaHabitacionService reservaService) {
        this.reservaService = reservaService;
    }

    // PUT http://localhost:8080/api/operador/reservas/{id}/activar
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            reservaService.activarEstadia(id);
            return ResponseEntity.ok(Map.of("mensaje", "Estadía activada correctamente."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT http://localhost:8080/api/operador/reservas/{id}/acabar
    @PutMapping("/{id}/acabar")
    public ResponseEntity<?> acabar(@PathVariable Long id) {
        try {
            reservaService.acabarEstadia(id);
            return ResponseEntity.ok(Map.of("mensaje", "Estadía finalizada correctamente."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}