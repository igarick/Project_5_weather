package org.weather.exception;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public String handleSessionErrors() {
        return "redirect:/auth/sign-in";
    }

    @ExceptionHandler(DaoException.class)
    public String handleDaoErrors(DaoException e, Model model) {
        model.addAttribute("errorMessage", e.getErrorInfo().getMessage());
        model.addAttribute("statusCode", e.getErrorInfo().getStatusCode());
        return "error";
    }

    @ExceptionHandler(MappingException.class)
    public String handleMappingErrors() {
        return "error";
    }

    @ExceptionHandler(DeserializationException.class)
    public String handleDeserializationErrors() {
        return "error";
    }


//
//    @ExceptionHandler(DuplicateUserException.class)
//    public String handleDuplicateError(DuplicateUserException e, BindingResult bindingResult) {
//        bindingResult.reject("login", e.getErrorInfo().getMessage());
//        return "auth/sign-up";
//    }

}
