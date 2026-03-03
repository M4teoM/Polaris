package com.polaris.controller;

import com.polaris.model.Habitacion;
import com.polaris.service.IHabitacionService;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Habitaciones físicas (número, piso, estado, tipo).
 * Rutas bajo /rooms para no colisionar con TipoHabitacionController (/habitaciones).
 */
@Controller
@RequestMapping("/rooms")
public class HabitacionController {

    @Autowired
    private IHabitacionService habitacionService;

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    // ── Utilidad: enriquecer una habitación con su TipoHabitacion ──────────
    private void enriquecer(Habitacion h) {
        if (h.getTipoHabitacionId() != null) {
            h.setTipoHabitacion(tipoHabitacionService.obtenerPorId(h.getTipoHabitacionId()));
        }
    }

    private void enriquecerLista(List<Habitacion> lista) {
        lista.forEach(this::enriquecer);
    }

    // ── Vista pública ──────────────────────────────────────────────────────
    @GetMapping
    public String listar(Model model) {
        List<Habitacion> lista = habitacionService.obtenerTodos();
        enriquecerLista(lista);
        model.addAttribute("rooms", lista);
        return "rooms/lista";
    }

    // ── Detalle público ────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Habitacion h = habitacionService.obtenerPorId(id);
        if (h == null) return "redirect:/rooms";
        enriquecer(h);
        model.addAttribute("room", h);
        return "rooms/detalle";
    }

    // ── Panel admin ────────────────────────────────────────────────────────
    @GetMapping("/admin")
    public String admin(Model model) {
        List<Habitacion> lista = habitacionService.obtenerTodos();
        enriquecerLista(lista);
        model.addAttribute("rooms", lista);
        return "rooms/lista-admin";
    }

    // ── Formulario nueva ───────────────────────────────────────────────────
    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("room", new Habitacion());
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        return "rooms/formulario";
    }

    @PostMapping("/nueva")
    public String nuevaGuardar(@ModelAttribute Habitacion habitacion) {
        habitacionService.crear(habitacion);
        return "redirect:/rooms/admin";
    }

    // ── Formulario editar ──────────────────────────────────────────────────
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Habitacion h = habitacionService.obtenerPorId(id);
        if (h == null) return "redirect:/rooms/admin";
        model.addAttribute("room", h);
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        return "rooms/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Long id,
                                @ModelAttribute Habitacion habitacion) {
        habitacion.setId(id);
        habitacionService.actualizar(habitacion);
        return "redirect:/rooms/admin";
    }

    // ── Eliminar ───────────────────────────────────────────────────────────
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        habitacionService.eliminar(id);
        return "redirect:/rooms/admin";
    }
}
