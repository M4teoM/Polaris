package com.polaris.controller;

import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/habitaciones")
public class TipoHabitacionController {

    @Autowired private ITipoHabitacionService tipoHabitacionService;
    @Autowired private IHabitacionService habitacionService;
    @Autowired private IClienteService clienteService;
    @Autowired private IServicioService servicioService;
    @Autowired private IReservaHabitacionService reservaService;

    // GET http://localhost:8080/habitaciones
    @GetMapping
    public String listar(@RequestParam(required = false) Long clienteId, Model model) {
        model.addAttribute("habitaciones", tipoHabitacionService.obtenerTodos());
        model.addAttribute("clienteId", clienteId);
        return "habitaciones/lista";
    }

    // GET http://localhost:8080/habitaciones/admin
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("habitaciones", tipoHabitacionService.obtenerTodos());
        List<Habitacion> rooms = habitacionService.obtenerTodos();
        rooms.forEach(h -> {
            if (h.getTipoHabitacionId() != null) {
                h.setTipoHabitacion(tipoHabitacionService.obtenerPorId(h.getTipoHabitacionId()));
            }
        });
        model.addAttribute("rooms", rooms);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("servicios", servicioService.obtenerTodos());
        model.addAttribute("reservas", reservaService.obtenerTodos());
        return "habitaciones/lista-admin";
    }

    // GET http://localhost:8080/habitaciones/{id}?clienteId={clienteId}
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id,
                          @RequestParam(required = false) Long clienteId,
                          Model model) {
        model.addAttribute("habitacion", tipoHabitacionService.obtenerPorId(id));
        model.addAttribute("clienteId", clienteId);
        return "habitaciones/detalle";
    }

    // GET http://localhost:8080/habitaciones/nueva
    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("habitacion", new TipoHabitacion());
        return "habitaciones/formulario";
    }

    // POST http://localhost:8080/habitaciones/nueva
    @PostMapping("/nueva")
    public String nuevaGuardar(@ModelAttribute TipoHabitacion habitacion) {
        tipoHabitacionService.crear(habitacion);
        return "redirect:/habitaciones/admin";
    }

    // GET http://localhost:8080/habitaciones/editar/{id}
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("habitacion", tipoHabitacionService.obtenerPorId(id));
        return "habitaciones/formulario";
    }

    // POST http://localhost:8080/habitaciones/editar/{id}
    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id, @ModelAttribute TipoHabitacion habitacion) {
        habitacion.setId(id);
        tipoHabitacionService.actualizar(habitacion);
        return "redirect:/habitaciones/admin";
    }

    // POST http://localhost:8080/habitaciones/eliminar/{id}
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tipoHabitacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Tipo de habitacion eliminado correctamente.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/habitaciones/admin";
    }
}