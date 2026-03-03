package com.polaris.controller;

import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import com.polaris.service.IHabitacionService;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/habitaciones")
public class TipoHabitacionController {

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    @Autowired
    private IHabitacionService habitacionService;

    // GET /habitaciones → vista pública (solo lectura)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("habitaciones", tipoHabitacionService.obtenerTodos());
        return "habitaciones/lista";
    }

    // GET /habitaciones/admin → panel de administración completo
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("habitaciones", tipoHabitacionService.obtenerTodos());

        // Cargar habitaciones físicas enriquecidas con su tipo
        List<Habitacion> rooms = habitacionService.obtenerTodos();
        rooms.forEach(h -> {
            if (h.getTipoHabitacionId() != null) {
                h.setTipoHabitacion(tipoHabitacionService.obtenerPorId(h.getTipoHabitacionId()));
            }
        });
        model.addAttribute("rooms", rooms);

        return "habitaciones/lista-admin";
    }

    // GET /habitaciones/{id} → detalle público
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        TipoHabitacion habitacion = tipoHabitacionService.obtenerPorId(id);
        if (habitacion == null) return "redirect:/habitaciones";
        model.addAttribute("habitacion", habitacion);
        return "habitaciones/detalle";
    }

    // GET /habitaciones/nueva → formulario vacío
    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("habitacion", new TipoHabitacion());
        return "habitaciones/formulario";
    }

    // POST /habitaciones/nueva → guarda la nueva
    @PostMapping("/nueva")
    public String nuevaGuardar(@ModelAttribute TipoHabitacion habitacion) {
        tipoHabitacionService.crear(habitacion);
        return "redirect:/habitaciones/admin";
    }

    // GET /habitaciones/editar/{id} → formulario con datos
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        TipoHabitacion habitacion = tipoHabitacionService.obtenerPorId(id);
        if (habitacion == null) return "redirect:/habitaciones/admin";
        model.addAttribute("habitacion", habitacion);
        return "habitaciones/formulario";
    }

    // POST /habitaciones/editar/{id} → guarda los cambios
    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id,
                                @ModelAttribute TipoHabitacion habitacion) {
        habitacion.setId(id);
        tipoHabitacionService.actualizar(habitacion);
        return "redirect:/habitaciones/admin";
    }

    // POST /habitaciones/eliminar/{id} → elimina y redirige al admin
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        tipoHabitacionService.eliminar(id);
        return "redirect:/habitaciones/admin";
    }
}