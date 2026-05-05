package com.polaris.controller;

import com.polaris.service.IReservaHabitacionService;
import com.polaris.service.IClienteService;
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

    @Autowired
    private IClienteService clienteService;

    // Formulario — llega desde "Reservar Ahora" en habitaciones/detalle.html
    // GET http://localhost:8080/reservas/nueva?tipoHabitacionId={tipoHabitacionId}&clienteId={clienteId}
    @GetMapping("/nueva")
    public String nuevaForm(@RequestParam Long tipoHabitacionId,
                            @RequestParam Long clienteId,
                            Model model) {
        model.addAttribute("tipo", tipoHabitacionService.obtenerPorId(tipoHabitacionId));
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("hoy", LocalDate.now().toString());
        return "reservas/formulario";
    }

    // GET http://localhost:8080/reservas/nueva/admin
    @GetMapping("/nueva/admin")
    public String nuevaFormAdmin(Model model) {
        model.addAttribute("tipos", tipoHabitacionService.obtenerTodos());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("hoy", LocalDate.now().toString());
        return "reservas/formulario-admin";
    }

    // Confirmar reserva
    // POST http://localhost:8080/reservas/nueva
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

    // POST http://localhost:8080/reservas/nueva/admin
    @PostMapping("/nueva/admin")
    public String nuevaGuardarAdmin(@RequestParam Long tipoHabitacionId,
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

        return "redirect:/habitaciones/admin?tab=reservas";
    }

    // Cancelar
    // POST http://localhost:8080/reservas/cancelar/{id}?clienteId={clienteId}&redirect={admin}
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id,
                           @RequestParam Long clienteId,
                           @RequestParam(required = false) String redirect) {
        reservaService.cancelar(id, clienteId);

        if ("admin".equals(redirect)) return "redirect:/habitaciones/admin?tab=reservas";
        return "redirect:/clientes/ver/" + clienteId;
    }
}