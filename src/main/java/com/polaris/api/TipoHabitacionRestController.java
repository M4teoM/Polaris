package com.polaris.api;

import com.polaris.model.TipoHabitacion;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoHabitacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}