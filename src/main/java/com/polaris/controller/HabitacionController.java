package com.polaris.controller;

import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
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

    // GET http://localhost:8080/rooms
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("rooms", habitacionService.obtenerTodos());
        return "rooms/lista";
    }

    // GET http://localhost:8080/rooms/{id}
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Habitacion h = habitacionService.obtenerPorId(id);
        if (h == null) return "redirect:/rooms";
        model.addAttribute("room", h);
        return "rooms/detalle";
    }

    // GET http://localhost:8080/rooms/admin
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("rooms", habitacionService.obtenerTodos());
        return "rooms/lista-admin";
    }

    // GET http://localhost:8080/rooms/nueva
    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("room", new Habitacion());
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        return "rooms/formulario";
    }

    // POST http://localhost:8080/rooms/nueva
    @PostMapping("/nueva")
    public String nuevaGuardar(@ModelAttribute Habitacion habitacion,
                               @RequestParam Long tipoHabitacionId) {
        TipoHabitacion tipo = tipoHabitacionService.obtenerPorId(tipoHabitacionId);
        habitacion.setTipoHabitacion(tipo);
        habitacionService.crear(habitacion);
        return "redirect:/rooms/admin";
    }

    // GET http://localhost:8080/rooms/editar/{id}
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Habitacion h = habitacionService.obtenerPorId(id);
        if (h == null) return "redirect:/rooms/admin";
        model.addAttribute("room", h);
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        return "rooms/formulario";
    }

    // POST http://localhost:8080/rooms/editar/{id}
    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id,
                                @ModelAttribute Habitacion habitacion,
                                @RequestParam Long tipoHabitacionId) {
        TipoHabitacion tipo = tipoHabitacionService.obtenerPorId(tipoHabitacionId);
        habitacion.setId(id);
        habitacion.setTipoHabitacion(tipo);
        habitacionService.actualizar(habitacion);
        return "redirect:/rooms/admin";
    }

    // POST http://localhost:8080/rooms/eliminar/{id}
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        habitacionService.eliminar(id);
        return "redirect:/rooms/admin";
    }
}