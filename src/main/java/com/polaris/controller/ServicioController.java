package com.polaris.controller;

import com.polaris.model.Servicio;
import com.polaris.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    // GET /servicios → tabla (entrega 1, se mantiene)
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicios-tabla";
    }

    // GET /servicios/cards → tarjetas con diseño Polaris
    // OJO: debe ir ANTES de /{id} para que "cards" no se confunda con un Long
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