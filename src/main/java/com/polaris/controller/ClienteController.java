package com.polaris.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polaris.model.Cliente;
import com.polaris.service.IClienteService;

import jakarta.servlet.http.HttpSession;

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
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente == null) return "redirect:/habitaciones/admin?tab=clientes";
        model.addAttribute("cliente", cliente);
        return "clientes/perfil";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping("/nuevo")
    public String nuevoGuardar(@ModelAttribute Cliente cliente, HttpSession session) {
        clienteService.crear(cliente);
        session.setAttribute("clienteActivo", cliente);
        return "redirect:/clientes/ver/" + cliente.getId();
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente == null) return "redirect:/habitaciones/admin?tab=clientes";
        model.addAttribute("cliente", cliente);
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