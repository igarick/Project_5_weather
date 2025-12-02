package org.weather.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CookieValidationException.class)
    public String handleSessionErrors() {
        return "redirect:/auth/sign-in";
    }

}
