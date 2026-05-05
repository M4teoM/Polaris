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

    // GET http://localhost:8080/servicios
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicios-tabla";
    }

    // GET http://localhost:8080/servicios/cards
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

    // GET http://localhost:8080/servicios/{id}
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("servicio", servicioService.obtenerPorId(id));
        return "servicio-detalle";
    }

    // GET http://localhost:8080/servicios/nuevo
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "servicios/formulario";
    }

    // POST http://localhost:8080/servicios/nuevo
    @PostMapping("/nuevo")
    public String crearServicio(@ModelAttribute Servicio servicio) {
        servicioService.crear(servicio);
        return "redirect:/habitaciones/admin?tab=servicios";
    }

    // GET http://localhost:8080/servicios/editar/{id}
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("servicio", servicioService.obtenerPorId(id));
        return "servicios/formulario";
    }

    // POST http://localhost:8080/servicios/editar/{id}
    @PostMapping("/editar/{id}")
    public String actualizarServicio(@PathVariable Long id, @ModelAttribute Servicio servicio) {
        servicio.setId(id);
        servicioService.actualizar(servicio);
        return "redirect:/habitaciones/admin?tab=servicios";
    }

    // POST http://localhost:8080/servicios/eliminar/{id}
    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id) {
        servicioService.eliminar(id);
        return "redirect:/habitaciones/admin?tab=servicios";
    }
}