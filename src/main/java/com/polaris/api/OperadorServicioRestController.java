package com.polaris.api;

import com.polaris.service.ContratacionServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST para las operaciones de contratación de servicios
 * realizadas por el operador desde el panel Angular.
 */
@RestController
@RequestMapping("/api/operador/servicios")
@CrossOrigin(origins = "http://localhost:4200")
public class OperadorServicioRestController {

    private final ContratacionServicioService contratacionServicioService;

    public OperadorServicioRestController(ContratacionServicioService contratacionServicioService) {
        this.contratacionServicioService = contratacionServicioService;
    }

    /** Busca la información de una habitación a partir de su número. */
    @GetMapping("/habitacion/{numeroHabitacion}")
    public ResponseEntity<?> buscarInfoHabitacion(@PathVariable String numeroHabitacion) {
        try {
            return ResponseEntity.ok(contratacionServicioService.obtenerInfoPorNumeroHabitacion(numeroHabitacion));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    /** Contrata un servicio para una habitación concreta. */
    @PostMapping("/contratar")
    public ResponseEntity<?> contratar(@RequestBody ContratarServicioRequest request) {
        try {
            return ResponseEntity.ok(
                    contratacionServicioService.contratarServicio(request.numeroHabitacion(), request.servicioId()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    /** Solicitud mínima para contratar un servicio desde el frontend. */
    public record ContratarServicioRequest(String numeroHabitacion, Long servicioId) {
    }
}
