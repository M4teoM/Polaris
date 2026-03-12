package com.polaris.controller;

import com.polaris.model.Servicio;
import com.polaris.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private IServicioService servicioService;

    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicios-tabla";
    }

    @GetMapping("/cards")
    public String listarCards(Model model) {
        List<Servicio> servicios = servicioService.obtenerTodos();
        List<String> categorias = servicios.stream()
                .map(Servicio::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("servicios", servicios);
        model.addAttribute("categorias", categorias);
        return "servicios-cards";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("servicio", servicioService.obtenerPorId(id));
        return "servicio-detalle";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "servicios/formulario";
    }

    @PostMapping("/nuevo")
    public String crearServicio(@ModelAttribute Servicio servicio) {
        servicioService.crear(servicio);
        return "redirect:/habitaciones/admin?tab=servicios";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("servicio", servicioService.obtenerPorId(id));
        return "servicios/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarServicio(@PathVariable Long id, @ModelAttribute Servicio servicio) {
        servicio.setId(id);
        servicioService.actualizar(servicio);
        return "redirect:/habitaciones/admin?tab=servicios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id) {
        servicioService.eliminar(id);
        return "redirect:/habitaciones/admin?tab=servicios";
    }
}