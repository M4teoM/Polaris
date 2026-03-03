package com.polaris.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polaris.model.Cliente;
import com.polaris.service.IClienteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private IClienteService clienteService;

    //Muestra el formulario de login
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    //Revisa correo y contraseña, guardando la sesion como cliente activo si hay alguna coincidencia o manda el mensaje de error
    @PostMapping("/login")
    public String loginProcesar(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {
        Optional<Cliente> resultado = clienteService.buscarPorCorreo(correo);

        if (resultado.isPresent() && resultado.get().getContrasena().equals(contrasena)) {
            session.setAttribute("clienteActivo", resultado.get());
            return "redirect:/perfil";
        }

        model.addAttribute("error", "Usuario o contraseña incorrecta");
        return "login";
    }


    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }

    //Cierra sesion
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

