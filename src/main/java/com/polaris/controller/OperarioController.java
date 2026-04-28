package com.polaris.controller;

import com.polaris.repository.ICuentaRepository;
import com.polaris.service.ContratacionServicioService;
import com.polaris.service.IReservaHabitacionService;
import com.polaris.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/operario")
public class OperarioController {

    @Autowired
    private IReservaHabitacionService reservaService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private ContratacionServicioService contratacionService;

    @Autowired
    private ICuentaRepository cuentaRepository;

    @GetMapping
    public String panel(Model model) {
        model.addAttribute("reservas",  reservaService.obtenerTodos());
        model.addAttribute("servicios", servicioService.obtenerTodos());
        model.addAttribute("cuentas",   cuentaRepository.findAllConDetalle());
        return "operario/panel";
    }

    @PostMapping("/reservas/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes ra) {
        try {
            reservaService.eliminar(id);
            ra.addFlashAttribute("successMessage", "Reserva eliminada.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/operario?tab=reservas";
    }

    @PostMapping("/reservas/activar/{id}")
    public String activarEstadia(@PathVariable Long id, RedirectAttributes ra) {
        try {
            reservaService.activarEstadia(id);
            ra.addFlashAttribute("successMessage", "Estadía activada correctamente.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/operario?tab=reservas";
    }

    @PostMapping("/reservas/acabar/{id}")
    public String acabarEstadia(@PathVariable Long id, RedirectAttributes ra) {
        try {
            reservaService.acabarEstadia(id);
            ra.addFlashAttribute("successMessage", "Estadía finalizada correctamente.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/operario?tab=reservas";
    }

    @PostMapping("/servicios/contratar")
    public String contratarServicio(@RequestParam String numeroHabitacion,
                                    @RequestParam Long servicioId,
                                    RedirectAttributes ra) {
        try {
            contratacionService.contratarServicio(numeroHabitacion, servicioId);
            ra.addFlashAttribute("successMessage",
                    "Servicio contratado para hab. " + numeroHabitacion + ".");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/operario?tab=servicios";
    }

    @PostMapping("/cuenta/{cuentaId}/item/{itemId}/eliminar")
    public String eliminarItemCuenta(@PathVariable Long cuentaId,
                                     @PathVariable Long itemId,
                                     RedirectAttributes ra) {
        try {
            contratacionService.eliminarItemCuenta(cuentaId, itemId);
            ra.addFlashAttribute("successMessage", "Servicio eliminado de la cuenta.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/operario?tab=cuentas";
    }

    @PostMapping("/item/{itemId}/pagar")
    public String pagarItem(@PathVariable Long itemId, RedirectAttributes ra) {
        try {
            contratacionService.pagarItem(itemId);
            ra.addFlashAttribute("successMessage", "Servicio pagado correctamente.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/operario?tab=cuentas";
    }
}