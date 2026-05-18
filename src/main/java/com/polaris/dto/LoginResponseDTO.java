package com.polaris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    // El JWT que Angular guarda en localStorage
    private String token;
    // "ROLE_ADMIN", "ROLE_OPERARIO" o "ROLE_CLIENTE"
    // Angular lo usa para saber a qué pantalla redirigir
    private String rol;
    // ID del UserEntity — útil si el frontend necesita hacer llamadas por id
    private Long   id;
    // Nombre para mostrar en la navbar ("Bienvenido, Samuel")
    private String nombre;
    private String correo;
}