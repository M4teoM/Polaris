package com.polaris.controller;

import com.polaris.model.Habitacion;
import com.polaris.service.IHabitacionService;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class HabitacionController {

    @Autowired
    private IHabitacionService habitacionService;

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("rooms", habitacionService.obtenerTodos());
        return "rooms/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("room", habitacionService.obtenerPorId(id));
        return "rooms/detalle";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("rooms", habitacionService.obtenerTodos());
        return "rooms/lista-admin";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("room", new Habitacion());
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        return "rooms/formulario";
    }

    @PostMapping("/nueva")
    public String nuevaGuardar(@ModelAttribute Habitacion habitacion,
                               @RequestParam Long tipoHabitacionId) {
        habitacion.setTipoHabitacion(tipoHabitacionService.obtenerPorId(tipoHabitacionId));
        habitacionService.crear(habitacion);
        return "redirect:/rooms/admin";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", habitacionService.obtenerPorId(id));
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        return "rooms/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id,
                                @ModelAttribute Habitacion habitacion,
                                @RequestParam Long tipoHabitacionId) {
        habitacion.setId(id);
        habitacion.setTipoHabitacion(tipoHabitacionService.obtenerPorId(tipoHabitacionId));
        habitacionService.actualizar(habitacion);
        return "redirect:/rooms/admin";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        habitacionService.eliminar(id);
        return "redirect:/rooms/admin";
    }
}