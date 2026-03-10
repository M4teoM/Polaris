package com.polaris.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message    = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri        = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int code = statusCode != null ? Integer.parseInt(statusCode.toString()) : 500;

        model.addAttribute("codigo", code);
        model.addAttribute("uri", uri != null ? uri : "/");

        if (code == 404) {
            model.addAttribute("titulo", "Página no encontrada");
            model.addAttribute("descripcion", "La página que buscas no existe o fue movida.");
            model.addAttribute("icono", "🔍");
        } else if (code == 403) {
            model.addAttribute("titulo", "Acceso denegado");
            model.addAttribute("descripcion", "No tienes permisos para acceder a este recurso.");
            model.addAttribute("icono", "🔒");
        } else {
            model.addAttribute("titulo", "Error del servidor");
            model.addAttribute("descripcion", message != null ? message.toString() : "Ocurrió un error inesperado.");
            model.addAttribute("icono", "⚠️");
        }

        return "error";
    }
}