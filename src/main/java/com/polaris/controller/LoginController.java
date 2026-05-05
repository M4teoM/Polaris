package com.polaris.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polaris.model.Cliente;
import com.polaris.model.Operario;
import com.polaris.service.IClienteService;
import com.polaris.service.IOperarioService;

@Controller
public class LoginController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IOperarioService operarioService;

    // GET http://localhost:8080/login
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // POST http://localhost:8080/login
    @PostMapping("/login")
    public String loginProcesar(@RequestParam String correo,
                                @RequestParam String contrasena,
                                Model model) {
        String correoNormalizado = correo == null ? "" : correo.trim();
        String contrasenaNormalizada = contrasena == null ? "" : contrasena.trim();

        Optional<Cliente> resultado = clienteService.buscarPorCorreo(correoNormalizado);

        if (resultado.isPresent() && resultado.get().getContrasena().equals(contrasenaNormalizada)) {
            return "redirect:/clientes/ver/" + resultado.get().getId();
        }

        Optional<Operario> operario = operarioService.buscarPorCorreo(correoNormalizado);

        if (operario.isPresent() && operario.get().getContrasena().equals(contrasenaNormalizada)) {
            return "redirect:/operario";
        }

        model.addAttribute("error", "Usuario o contraseña incorrecta");
        return "login";
    }
}