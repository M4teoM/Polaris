package com.polaris.controller;

import com.polaris.model.Servicio;
import com.polaris.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private IServicioService servicioService;

    // GET /servicios → tabla 
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicios-tabla";
    }

    // GET /servicios/cards → tarjetas con diseño Polaris
    @GetMapping("/cards")
    public String listarCards(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicios-cards";
    }

    // GET /servicios/{id} → detalle de un servicio
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Servicio servicio = servicioService.obtenerPorId(id);

        if (servicio == null) {
            return "redirect:/servicios/cards";
        }

        model.addAttribute("servicio", servicio);
        return "servicio-detalle";
    }
}