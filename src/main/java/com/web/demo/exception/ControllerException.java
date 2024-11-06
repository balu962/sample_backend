package com.web.demo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerException {
    @ExceptionHandler(DataNotFoundException.class)
	public String handleDataNotFoundException(DataNotFoundException err, Model model) {
        model.addAttribute("errorMessage", err.getMessage());
        return "lib/error404";
    }
}
