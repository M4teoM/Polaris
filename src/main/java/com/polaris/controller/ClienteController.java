package com.polaris.controller;

import com.polaris.model.Cliente;
import com.polaris.service.IClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "clientes/lista-admin";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.obtenerPorId(id));
        return "clientes/perfil";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping("/nuevo")
    public String nuevoGuardar(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.crear(cliente);
            return "redirect:/clientes/ver/" + cliente.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("errorCorreo", e.getMessage());
            return "clientes/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.obtenerPorId(id));
        return "clientes/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id, @ModelAttribute Cliente cliente, HttpSession session) {
        cliente.setId(id);
        clienteService.actualizar(cliente);
        session.setAttribute("clienteActivo", cliente);
        return "redirect:/clientes/ver/" + id;
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        clienteService.eliminar(id);
        session.invalidate();
        return "redirect:/";
    }
}