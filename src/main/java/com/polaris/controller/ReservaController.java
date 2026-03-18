package com.polaris.controller;

import com.polaris.service.IReservaHabitacionService;
import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private IReservaHabitacionService reservaService;

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    // Formulario — llega desde "Reservar Ahora" en habitaciones/detalle.html
    @GetMapping("/nueva")
    public String nuevaForm(@RequestParam Long tipoHabitacionId,
                            @RequestParam Long clienteId,
                            Model model) {
        model.addAttribute("tipo", tipoHabitacionService.obtenerPorId(tipoHabitacionId));
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("hoy", LocalDate.now().toString());
        return "reservas/formulario";
    }

    // Confirmar reserva
    @PostMapping("/nueva")
    public String nuevaGuardar(@RequestParam Long tipoHabitacionId,
                               @RequestParam Long clienteId,
                               @RequestParam String fechaCheckIn,
                               @RequestParam String fechaCheckOut,
                               @RequestParam int numeroHuespedes) {

        reservaService.crearDesdeDetalle(
                clienteId,
                tipoHabitacionId,
                LocalDate.parse(fechaCheckIn),
                LocalDate.parse(fechaCheckOut),
                numeroHuespedes);

        return "redirect:/clientes/ver/" + clienteId;
    }

    // Cancelar
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id,
                           @RequestParam Long clienteId,
                           @RequestParam(required = false) String redirect) {
        reservaService.cancelar(id, clienteId);

        if ("admin".equals(redirect)) return "redirect:/habitaciones/admin?tab=reservas";
        return "redirect:/clientes/ver/" + clienteId;
    }
}