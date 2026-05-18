package com.polaris.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS — permite llamadas desde Angular en localhost:4200
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Deshabilitamos CSRF porque usamos JWT stateless, no cookies de sesión
            .csrf(csrf -> csrf.disable())
            // Sin sesión — cada request se autentica solo con el token
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // ── Reglas de autorización por endpoint ──────────────────────
            .authorizeHttpRequests(auth -> auth

                // Rutas públicas — no necesitan token
                .requestMatchers(
                    "/api/auth/**",
                    "/api/tipos-habitacion",
                    "/api/habitaciones",
                    "/api/servicios",
                    "/api/testimonios/**",
                    "/h2/**",
                    "/h2-console/**"
                ).permitAll()

                // Solo ADMIN puede gestionar clientes, operarios, habitaciones y servicios
                .requestMatchers(
                    "/api/clientes/**",
                    "/api/operarios/**",
                    "/api/habitaciones/**",
                    "/api/tipos-habitacion/**",
                    "/api/servicios/**"
                ).hasRole("ADMIN")

                // ADMIN y OPERARIO pueden ver cuentas y operaciones de operador
                .requestMatchers(
                    "/api/operador/**",
                    "/api/cuentas/**"
                ).hasAnyRole("ADMIN", "OPERARIO")

                // ADMIN y CLIENTE pueden ver y crear reservas
                .requestMatchers(
                    "/api/reservas/**",
                    "/api/portal/**"
                ).hasAnyRole("ADMIN", "CLIENTE")

                // Cualquier otra ruta requiere estar autenticado
                .anyRequest().authenticated()
            )
            // Deshabilitamos el login de Thymeleaf — usamos Angular
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            // Registramos nuestro filtro JWT antes del filtro estándar de usuario/contraseña
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Necesario para que la consola H2 funcione en desarrollo (usa iframes)
        http.headers(headers -> headers.frameOptions(fo -> fo.disable()));

        return http.build();
    }

    // BCrypt es el algoritmo estándar para guardar contraseñas de forma segura
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expone el AuthenticationManager como Bean para que lo use el AuthRestController
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configuración CORS — define qué orígenes, métodos y headers están permitidos
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}