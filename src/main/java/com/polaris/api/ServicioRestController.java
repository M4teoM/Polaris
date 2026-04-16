package com.polaris.api;

import com.polaris.model.Servicio;
import com.polaris.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioRestController {

    @Autowired
    private IServicioService servicioService;

    @GetMapping
    public List<Servicio> listar() {
        return servicioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        servicioService.crear(servicio);
        return ResponseEntity.ok(servicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        servicio.setId(id);
        servicioService.actualizar(servicio);
        return ResponseEntity.ok(servicio);
    }

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