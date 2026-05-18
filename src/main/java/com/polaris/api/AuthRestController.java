package com.polaris.api;

import com.polaris.dto.LoginRequestDTO;
import com.polaris.dto.LoginResponseDTO;
import com.polaris.model.UserEntity;
import com.polaris.repository.IUserRepository;
import com.polaris.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserRepository userRepository;

    /**
     * POST /api/auth/login
     * Body: { "correo": "admin@polaris.com", "contrasena": "admin123" }
     * Respuesta exitosa: { token, rol, id, nombre, correo }
     * Respuesta fallida: 401 { "error": "Credenciales incorrectas" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            // Le pedimos a Spring Security que verifique las credenciales
            // Internamente llama a UserDetailsServiceImpl.loadUserByUsername
            // y compara la contraseña con BCrypt
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getCorreo(),
                    request.getContrasena()
                )
            );

            // Si llegamos aquí, las credenciales son correctas
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            // Generamos el JWT
            String token = jwtUtil.generateToken(userDetails);

            // Buscamos el UserEntity para devolver info adicional al frontend
            UserEntity user = userRepository.findByCorreo(request.getCorreo())
                    .orElseThrow();

            return ResponseEntity.ok(new LoginResponseDTO(
                token,
                user.getRol().name(),
                user.getEntidadId(),
                user.getNombre(),
                user.getCorreo()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
    }

    /**
     * GET /api/auth/me
     * Requiere header: Authorization: Bearer <token>
     * Devuelve los datos del usuario autenticado actualmente
     * Útil para que Angular verifique si el token sigue siendo válido
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "No autenticado"));
        }

        UserEntity user = userRepository.findByCorreo(authentication.getName())
                .orElseThrow();

        // Devolvemos los datos del usuario sin generar un token nuevo
        return ResponseEntity.ok(new LoginResponseDTO(
            null,
            user.getRol().name(),
            user.getEntidadId(),
            user.getNombre(),
            user.getCorreo()
        ));
    }

    /**
     * POST /api/auth/logout
     * Con JWT el logout es responsabilidad del frontend (borra el token
     * de localStorage). Este endpoint existe para documentarlo en Postman
     * y para que el profesor pueda probarlo.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
            Map.of("mensaje", "Sesión cerrada. Elimina el token del cliente.")
        );
    }
}