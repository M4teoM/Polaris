package com.polaris.api;

import com.polaris.model.Operario;
import com.polaris.service.IOperarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la autenticación y consulta básica de operarios.
 */
@RestController
@RequestMapping("/api/operarios")
@CrossOrigin(origins = "http://localhost:4200")
public class OperarioRestController {

    @Autowired
    private IOperarioService operarioService;

    /** Devuelve el listado de operarios registrados. */
    // GET http://localhost:8080/api/operarios
    @GetMapping
    public List<Operario> listar() {
        return operarioService.obtenerTodos();
    }

}
