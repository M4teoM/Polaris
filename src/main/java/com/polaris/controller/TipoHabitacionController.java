package com.polaris.controller;

import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.service.IClienteService;
import com.polaris.service.IHabitacionService;
import com.polaris.service.IServicioService;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/habitaciones")
public class TipoHabitacionController {

    @Autowired private ITipoHabitacionService tipoHabitacionService;
    @Autowired private IHabitacionService habitacionService;
    @Autowired private IClienteService clienteService;
    @Autowired private IServicioService servicioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("habitaciones", tipoHabitacionService.obtenerTodos());
        return "habitaciones/lista";
    }

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
        return "habitaciones/lista-admin";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("habitacion", tipoHabitacionService.obtenerPorId(id));
        return "habitaciones/detalle";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("habitacion", new TipoHabitacion());
        return "habitaciones/formulario";
    }

    @PostMapping("/nueva")
    public String nuevaGuardar(@ModelAttribute TipoHabitacion habitacion) {
        tipoHabitacionService.crear(habitacion);
        return "redirect:/habitaciones/admin";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("habitacion", tipoHabitacionService.obtenerPorId(id));
        return "habitaciones/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id, @ModelAttribute TipoHabitacion habitacion) {
        habitacion.setId(id);
        tipoHabitacionService.actualizar(habitacion);
        return "redirect:/habitaciones/admin";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        tipoHabitacionService.eliminar(id);
        return "redirect:/habitaciones/admin";
    }
}