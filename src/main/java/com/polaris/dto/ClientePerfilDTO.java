package com.polaris.dto;

/**
 * DTO plano para exponer la información de perfil del cliente en el portal de usuario.
 *
 * Este objeto evita devolver la entidad Cliente completa y únicamente publica
 * los datos necesarios para la vista del perfil.
 */
public class ClientePerfilDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String cedula;
    private String telefono;

    public ClientePerfilDTO() {
    }

    public ClientePerfilDTO(Long id, String nombre, String apellido, String correo, String cedula, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.cedula = cedula;
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
