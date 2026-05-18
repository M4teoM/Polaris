package com.polaris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperarioDTO {
    private Long   id;
    private String nombre;
    private String correo;
    // No incluimos contrasena ni el objeto Administrador anidado
    // El frontend solo necesita saber el nombre y correo del operario
}