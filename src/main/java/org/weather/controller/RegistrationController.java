package org.weather.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.InputUserRegistrationDto;
import org.weather.dto.SessionIdDto;
import org.weather.exception.DuplicateUserException;
import org.weather.service.SessionService;
import org.weather.service.UserService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class RegistrationController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieParamValidatorAndHandler validatorAndHandler;

    @Autowired
    public RegistrationController(UserService userService, SessionService sessionService, CookieParamValidatorAndHandler validatorAndHandler) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.validatorAndHandler = validatorAndHandler;
    }

    @GetMapping("/sign-up")
    public String showSignUpForm(@ModelAttribute("userDto") InputUserRegistrationDto userDto) {
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute("userDto") @Valid InputUserRegistrationDto userDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/sign-up";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.reject("", "Passwords don't match");
            return "auth/sign-up";
        }

        try {
            userService.registerUser(userDto);
        } catch (DuplicateUserException e) {
            bindingResult.rejectValue("login", "", e.getErrorInfo().getMessage());
            return "auth/sign-up";
        }
        return "redirect:/auth/sign-in";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam) {
        if (!StringUtils.hasText(sessionIdParam)) {
            return "redirect:/auth/sign-in";
        }

        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);
        validatorAndHandler.validateSessionExists(sessionId);

        SessionIdDto sessionIdDto = new SessionIdDto(sessionId);
        sessionService.deactivateSession(sessionIdDto);

        return "redirect:/auth/sign-in";
    }
}
