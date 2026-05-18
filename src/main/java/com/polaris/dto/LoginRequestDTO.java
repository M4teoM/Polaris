package com.polaris.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    // Correo y contraseña que Angular envía en el body del POST /api/auth/login
    private String correo;
    private String contrasena;
}