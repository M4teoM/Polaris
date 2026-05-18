package com.polaris.security;

import com.polaris.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    // Spring Security llama este método automáticamente cuando alguien
    // intenta autenticarse — recibe el correo y devuelve el UserDetails
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        return userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con correo: " + correo));
    }
}