package com.polaris.api;

import com.polaris.model.Servicio;
import com.polaris.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el catálogo de servicios.
 * Expone operaciones CRUD básicas y una eliminación forzada cuando
 * el servicio tiene relaciones que impiden el borrado normal.
 */
@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioRestController {

    @Autowired
    private IServicioService servicioService;

    /** Devuelve todos los servicios registrados. */
    // GET http://localhost:8080/api/servicios
    @GetMapping
    public List<Servicio> listar() {
        return servicioService.obtenerTodos();
    }

    /** Obtiene un servicio por su identificador. */
    // GET http://localhost:8080/api/servicios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    /** Crea un nuevo servicio a partir del JSON recibido. */
    // POST http://localhost:8080/api/servicios
    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        servicioService.crear(servicio);
        return ResponseEntity.ok(servicio);
    }

    /** Actualiza un servicio existente usando el ID de la URL. */
    // PUT http://localhost:8080/api/servicios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        servicio.setId(id);
        servicioService.actualizar(servicio);
        return ResponseEntity.ok(servicio);
    }

    /** Elimina un servicio, con opción de forzar el borrado si existe dependencia. */
    // DELETE http://localhost:8080/api/servicios/{id}?force={true|false}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                      @RequestParam(defaultValue = "false") boolean force) {
        try {
            if (force) {
                servicioService.eliminarForzado(id);
            } else {
                servicioService.eliminar(id);
            }
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}