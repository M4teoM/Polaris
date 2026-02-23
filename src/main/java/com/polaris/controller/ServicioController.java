package com.polaris.controller;

import com.polaris.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService service;

    public ServicioController(ServicioService service) {
        this.service = service;
    }

    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", service.obtenerTodos());
        return "servicios-tabla";
    }

    @GetMapping("/{id}")
    public String verServicio(@PathVariable Long id, Model model) {
        model.addAttribute("servicio", service.obtenerPorId(id));
        return "servicio-detalle";
    }

    @GetMapping("/cards")
    public String listarCards(Model model) {
        model.addAttribute("servicios", service.obtenerTodos());
        return "servicios-cards";
    }
}