package com.polaris.api;

import com.polaris.model.Cliente;
import com.polaris.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cliente cliente) {
        try {
            clienteService.crear(cliente);
            return ResponseEntity.ok(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id,
                                              @RequestBody Cliente cliente) {
        cliente.setId(id);
        clienteService.actualizar(cliente);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                      @RequestParam(defaultValue = "false") boolean force) {
        try {
            if (force) {
                clienteService.eliminarForzado(id);
            } else {
                clienteService.eliminar(id);
            }
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Login — Angular manda { correo, contrasena }, recibe el cliente o 401
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo");
        String contrasena = credenciales.get("contrasena");

        Optional<Cliente> resultado = clienteService.buscarPorCorreo(correo);

        if (resultado.isPresent() && resultado.get().getContrasena().equals(contrasena)) {
            return ResponseEntity.ok(resultado.get());
        }

        return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
    }
}