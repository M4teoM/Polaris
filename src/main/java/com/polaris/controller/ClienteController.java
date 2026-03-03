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


//Se maneja todo el crud en lo que trata de cliente
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    //Muestra todos los clientes como admin
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "clientes/lista-admin";
    }

    //Muestra el formulario y crea un cliente nuevo
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    //Guarda el cliente nuevo en base a los datos
    @PostMapping("/nuevo")
    public String nuevoGuardar(@ModelAttribute Cliente cliente) {
        clienteService.crear(cliente);
        return "redirect:/perfil";
    }

    //Edita el perfil de cliente en base a su id, pero si no aparece reenvia al panel de admin
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente == null) return "redirect:/clientes/admin";
        model.addAttribute("cliente", cliente);
        return "clientes/formulario";
    }

    //Se actualiza los datos
    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id, @ModelAttribute Cliente cliente) {
        cliente.setId(id);
        clienteService.actualizar(cliente);
        return "redirect:/perfil";
    }

    //Elimina el cliente con la id correspondiente y regresa al panel de admin
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes/admin";
    }
}