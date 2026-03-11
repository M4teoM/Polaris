package com.polaris.controller;

import com.polaris.model.Servicio;
import com.polaris.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

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

    // GET /servicios/nuevo → formulario de creación
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "servicios/formulario";
    }

    // POST /servicios/nuevo → guardar nuevo servicio
    @PostMapping("/nuevo")
    public String crearServicio(@ModelAttribute Servicio servicio) {
        servicioService.crear(servicio);
        return "redirect:/habitaciones/admin?tab=servicios";
    }

    // GET /servicios/editar/{id} → formulario de edición
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Servicio servicio = servicioService.obtenerPorId(id);
        if (servicio == null) return "redirect:/habitaciones/admin?tab=servicios";
        model.addAttribute("servicio", servicio);
        return "servicios/formulario";
    }

    // POST /servicios/editar/{id} → guardar cambios
    @PostMapping("/editar/{id}")
    public String actualizarServicio(@PathVariable Long id, @ModelAttribute Servicio servicio) {
        servicio.setId(id);
        servicioService.actualizar(servicio);
        return "redirect:/habitaciones/admin?tab=servicios";
    }

    // POST /servicios/eliminar/{id} → eliminar servicio
    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id) {
        servicioService.eliminar(id);
        return "redirect:/habitaciones/admin?tab=servicios";
    }
}