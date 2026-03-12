package com.polaris.errors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorUserNotFoundException.class)
    public String handleUserNotFound(ErrorUserNotFoundException ex, Model model) {
        model.addAttribute("icono", "👤");
        model.addAttribute("codigo", 404);
        model.addAttribute("titulo", "Usuario no encontrado");
        model.addAttribute("descripcion", ex.getMessage());
        model.addAttribute("uri", "");
        return "error";
    }

    @ExceptionHandler(ErrorRoomNotFoundException.class)
    public String handleRoomNotFound(ErrorRoomNotFoundException ex, Model model) {
        model.addAttribute("icono", "🛏️");
        model.addAttribute("codigo", 404);
        model.addAttribute("titulo", "Habitación no encontrada");
        model.addAttribute("descripcion", ex.getMessage());
        model.addAttribute("uri", "");
        return "error";
    }

    @ExceptionHandler(ErrorServiceNotFoundException.class)
    public String handleServiceNotFound(ErrorServiceNotFoundException ex, Model model) {
        model.addAttribute("icono", "🛎️");
        model.addAttribute("codigo", 404);
        model.addAttribute("titulo", "Servicio no encontrado");
        model.addAttribute("descripcion", ex.getMessage());
        model.addAttribute("uri", "");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("icono", "⚠️");
        model.addAttribute("codigo", 500);
        model.addAttribute("titulo", "Error del servidor");
        model.addAttribute("descripcion", "Ocurrió un error inesperado: " + ex.getMessage());
        model.addAttribute("uri", "");
        return "error";
    }
}