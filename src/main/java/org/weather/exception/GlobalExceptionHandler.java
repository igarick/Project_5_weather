package org.weather.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public String handleSessionErrors() {
        return "redirect:/auth/sign-in";
    }

    @ExceptionHandler(DaoException.class)
    public String handleDaoErrors() {
        return "redirect:/error";
    }

    @ExceptionHandler(MappingException.class)
    public String handleMappingErrors() {
        return "redirect:/error";
    }

}
