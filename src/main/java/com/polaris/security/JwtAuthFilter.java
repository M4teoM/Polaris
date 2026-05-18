package com.polaris.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Este método se ejecuta UNA VEZ por cada request que llega al backend
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Si no viene el header Authorization o no empieza con "Bearer ",
        // dejamos pasar la request sin autenticar (las rutas públicas funcionan así)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Quitamos el prefijo "Bearer " para quedarnos solo con el token
        String token = authHeader.substring(7);
        String correo;

        try {
            correo = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            // Token malformado o inválido — dejamos pasar sin autenticar
            filterChain.doFilter(request, response);
            return;
        }

        // Si obtuvimos el correo y todavía no hay autenticación en el contexto
        if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                // Creamos el objeto de autenticación y lo metemos en el contexto
                // A partir de aquí Spring Security sabe quién es el usuario
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}