package com.polaris.dto;

/**
 * DTO de perfil de cliente — expone solo los datos necesarios para
 * la vista de perfil del portal. Nunca incluye la contraseña.
 */
public class ClientePerfilDTO {

    private Long   id;
    private String nombre;
    private String apellido;
    private String correo;
    private String cedula;
    private String telefono;

    // Constructor vacío requerido por Jackson
    public ClientePerfilDTO() {}

    // Constructor completo usado en PortalUsuarioService para armar la respuesta
    public ClientePerfilDTO(Long id, String nombre, String apellido,
                             String correo, String cedula, String telefono) {
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.correo   = correo;
        this.cedula   = cedula;
        this.telefono = telefono;
    }

    // ── Getters y Setters ────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}