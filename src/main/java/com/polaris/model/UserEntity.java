package com.polaris.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_entity")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El correo es el "username" que Spring Security usa para identificar al usuario
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    // La contraseña siempre se guarda encriptada con BCrypt (nunca en texto plano)
    @Column(nullable = false, length = 200)
    private String contrasena;

    // Nombre visible del usuario (para mostrarlo en la navbar del frontend)
    @Column(length = 100)
    private String nombre;

    // El rol determina a qué endpoints puede acceder
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role rol;

    // ID del registro en la tabla original (cliente_id, operario_id, etc.)
    // Sirve para cargar el objeto completo cuando lo necesites
    @Column(name = "entidad_id")
    private Long entidadId;

    // ── Métodos que exige la interfaz UserDetails de Spring Security ──────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Le dice a Spring Security qué rol tiene este usuario
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        // Spring Security llama este método para obtener la contraseña encriptada
        return contrasena;
    }

    @Override
    public String getUsername() {
        // Spring Security usa el correo como identificador único
        return correo;
    }

    // Estos cuatro métodos deben retornar true para que la cuenta funcione
    @Override
    public boolean isAccountNonExpired()     { return true; }
    @Override
    public boolean isAccountNonLocked()      { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled()               { return true; }
}