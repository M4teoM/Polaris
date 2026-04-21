package com.polaris.api;

import com.polaris.model.Operario;
import com.polaris.service.IOperarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/operarios")
@CrossOrigin(origins = "http://localhost:4200")
public class OperarioRestController {

    @Autowired
    private IOperarioService operarioService;

    @GetMapping
    public List<Operario> listar() {
        return operarioService.obtenerTodos();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo");
        String contrasena = credenciales.get("contrasena");

        Optional<Operario> resultado = operarioService.buscarPorCorreo(correo);

        if (resultado.isPresent() && resultado.get().getContrasena().equals(contrasena)) {
            Operario operario = resultado.get();
            return ResponseEntity.ok(Map.of(
                    "id", operario.getId(),
                    "correo", operario.getCorreo(),
                    "nombre", operario.getNombre()
            ));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
    }
}
