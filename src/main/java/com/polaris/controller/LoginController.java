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

@Controller
public class LoginController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginProcesar(@RequestParam String correo,
                                @RequestParam String contrasena,
                                Model model) {
        Optional<Cliente> resultado = clienteService.buscarPorCorreo(correo);

        if (resultado.isPresent() && resultado.get().getContrasena().equals(contrasena)) {
            return "redirect:/clientes/ver/" + resultado.get().getId();
        }

        model.addAttribute("error", "Usuario o contraseña incorrecta");
        return "login";
    }
}