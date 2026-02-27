package com.polaris.controller;

import com.polaris.service.ITipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ITipoHabitacionService tipoHabitacionService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("habitaciones", tipoHabitacionService.obtenerTodos());
        return "index";
    }
}