package com.polaris.controller;

import com.polaris.model.Cliente;
import com.polaris.service.IClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.polaris.service.IReservaHabitacionService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IReservaHabitacionService reservaService;

    // GET http://localhost:8080/clientes/admin
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "clientes/lista-admin";
    }

    // GET http://localhost:8080/clientes/ver/{id}
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.obtenerPorId(id));
        model.addAttribute("reservas", reservaService.obtenerPorCliente(id));
        return "clientes/perfil";
    }

    // GET http://localhost:8080/clientes/nuevo
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    // POST http://localhost:8080/clientes/nuevo
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

    // GET http://localhost:8080/clientes/editar/{id}
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.obtenerPorId(id));
        return "clientes/formulario";
    }

    // POST http://localhost:8080/clientes/editar/{id}
    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id, @ModelAttribute Cliente cliente, HttpSession session) {
        cliente.setId(id);
        clienteService.actualizar(cliente);
        session.setAttribute("clienteActivo", cliente);
        return "redirect:/clientes/ver/" + id;
    }

    // POST http://localhost:8080/clientes/eliminar/{id}
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            session.invalidate();
            return "redirect:/";
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("aviso", ex.getMessage());
            return "redirect:/clientes/ver/" + id;
        }
    }
}