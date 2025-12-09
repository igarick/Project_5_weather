package org.weather.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.weather.exception.app.SessionNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public String handleSessionErrors() {
        return "redirect:/auth/sign-in";
    }

    @ExceptionHandler(BaseException.class)
    public String handleBasesErrors(BaseException e, Model model) {
        model.addAttribute("errorMessage", e.getErrorInfo().getMessage());
        model.addAttribute("statusCode", e.getErrorInfo().getStatusCode());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralErrors(Exception e, Model model) {
        log.error("Unknown error", e);
        model.addAttribute("errorMessage", ErrorInfo.UNKNOWN_ERROR.getMessage());
        model.addAttribute("statusCode", ErrorInfo.UNKNOWN_ERROR.getStatusCode());
        return "error";
    }
}
