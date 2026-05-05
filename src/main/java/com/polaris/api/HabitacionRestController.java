package com.polaris.api;

import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.service.IHabitacionService;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la administración de habitaciones físicas.
 * Trabaja con DTOs planos para simplificar el consumo desde Angular.
 */
@RestController
@RequestMapping("/api/habitaciones-fisicas")
@CrossOrigin(origins = "http://localhost:4200")
public class HabitacionRestController {

    @Autowired
    private IHabitacionService habitacionService;

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    /** Lista todas las habitaciones en formato plano. */
    // GET http://localhost:8080/api/habitaciones-fisicas
    @GetMapping
    public List<HabitacionResponse> listar() {
        return habitacionService.obtenerTodos().stream().map(this::toResponse).toList();
    }

    /** Obtiene una habitación física por su ID. */
    // GET http://localhost:8080/api/habitaciones-fisicas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponse> obtener(@PathVariable Long id) {
        Habitacion habitacion = habitacionService.obtenerPorId(id);
        return ResponseEntity.ok(toResponse(habitacion));
    }

    /** Crea una nueva habitación física validando primero la solicitud. */
    // POST http://localhost:8080/api/habitaciones-fisicas
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody HabitacionRequest request) {
        try {
            String validationError = validateRequest(request);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(Map.of("error", validationError));
            }

            TipoHabitacion tipo = tipoHabitacionService.obtenerPorId(request.tipoHabitacionId());
            Habitacion habitacion = new Habitacion();
            habitacion.setNumero(request.numero().trim());
            habitacion.setPiso(request.piso());
            habitacion.setEstado(request.estado());
            habitacion.setTipoHabitacion(tipo);

            habitacionService.crear(habitacion);
            return ResponseEntity.ok(toResponse(habitacion));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe una habitación con ese número."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT http://localhost:8080/api/habitaciones-fisicas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody HabitacionRequest request) {
        try {
            String validationError = validateRequest(request);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(Map.of("error", validationError));
            }

            Habitacion existente = habitacionService.obtenerPorId(id);
            TipoHabitacion tipo = tipoHabitacionService.obtenerPorId(request.tipoHabitacionId());

            existente.setNumero(request.numero().trim());
            existente.setPiso(request.piso());
            existente.setEstado(request.estado());
            existente.setTipoHabitacion(tipo);

            habitacionService.actualizar(existente);
            return ResponseEntity.ok(toResponse(existente));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe una habitación con ese número."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE http://localhost:8080/api/habitaciones-fisicas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            habitacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Convierte la entidad de dominio a un DTO plano para la API. */
    private HabitacionResponse toResponse(Habitacion habitacion) {
        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getNumero(),
                habitacion.getPiso(),
                habitacion.getEstado(),
                habitacion.getTipoHabitacionId()
        );
    }

    /** Valida los campos obligatorios antes de crear o actualizar. */
    private String validateRequest(HabitacionRequest request) {
        if (request == null) {
            return "No se recibieron datos de la habitación.";
        }
        if (request.numero() == null || request.numero().trim().isEmpty()) {
            return "El número de habitación es obligatorio.";
        }
        if (request.piso() == null || request.piso() < 1) {
            return "El piso debe ser mayor a 0.";
        }
        if (request.estado() == null || request.estado().trim().isEmpty()) {
            return "El estado de la habitación es obligatorio.";
        }
        if (request.tipoHabitacionId() == null || request.tipoHabitacionId() < 1) {
            return "Debes seleccionar un tipo de habitación válido.";
        }
        return null;
    }

    /** Datos mínimos requeridos para crear o actualizar una habitación física. */
    public record HabitacionRequest(
            String numero,
            Integer piso,
            String estado,
            Long tipoHabitacionId
    ) {
    }

    /** Respuesta plana que evita exponer la entidad completa al frontend. */
    public record HabitacionResponse(
            Long id,
            String numero,
            Integer piso,
            String estado,
            Long tipoHabitacionId
    ) {
    }
}
