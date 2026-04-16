package com.polaris.api;

import com.polaris.model.TipoHabitacion;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoHabitacionRestController {

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    @GetMapping
    public List<TipoHabitacion> listar() {
        return tipoHabitacionService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoHabitacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(tipoHabitacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoHabitacion> crear(@RequestBody TipoHabitacion tipo) {
        tipoHabitacionService.crear(tipo);
        return ResponseEntity.ok(tipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoHabitacion> actualizar(@PathVariable Long id,
                                                     @RequestBody TipoHabitacion tipo) {
        tipo.setId(id);
        tipoHabitacionService.actualizar(tipo);
        return ResponseEntity.ok(tipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                      @RequestParam(defaultValue = "false") boolean force) {
        try {
            if (force) {
                tipoHabitacionService.eliminarForzado(id);
            } else {
                tipoHabitacionService.eliminar(id);
            }
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}