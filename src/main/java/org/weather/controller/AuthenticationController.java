package org.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.InputUserLoginDto;
import org.weather.dto.InputUserRegistrationDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.DuplicateUserException;
import org.weather.exception.InvalidUserOrPasswordException;
import org.weather.exception.SessionNotFoundException;
import org.weather.service.SessionService;
import org.weather.service.UserService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieParamValidatorAndHandler validatorAndHandler;

    @Autowired
    public AuthenticationController(UserService userService, SessionService sessionService, CookieParamValidatorAndHandler validatorAndHandler) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.validatorAndHandler = validatorAndHandler;
    }


    @GetMapping("/sign-in")
    public String showSignInForm(@ModelAttribute("userDto") InputUserLoginDto userDto) {
        return "auth/sign-in";
    }

    @PostMapping("/sign-in")
    public String processSignIn(@ModelAttribute("userDto") @Valid InputUserLoginDto userDto,
                                BindingResult bindingResult,
                                @CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                                HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "auth/sign-in";
        }

        UUID sessionIdFromCookies = null;
        try {
            sessionIdFromCookies = validatorAndHandler.extractSessionId(sessionIdParam);
        } catch (SessionNotFoundException e) {
            sessionIdFromCookies = null;
        }

        UserIdDto userId;
        try {
            userId = userService.authenticateUser(userDto);
        } catch (InvalidUserOrPasswordException e) {
            bindingResult.reject("", e.getErrorInfo().getMessage());
            return "auth/sign-in";
        }

        SessionIdDto currentSessionIdDto;
        if (sessionIdFromCookies == null) {
            currentSessionIdDto = sessionService.createSession(userId);
        } else {
            Optional<SessionIdDto> currentOptional = sessionService.findCurrentSession(new SessionIdDto(sessionIdFromCookies));
            currentSessionIdDto = currentOptional.orElseGet(() -> sessionService.createSession(userId));
        }

        String sessionIdStr = String.valueOf(currentSessionIdDto.getSessionId());
        ResponseCookie sessionIdCookie = ResponseCookie.from("sessionId", sessionIdStr)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 10)
                .build();
        response.addHeader("Set-Cookie", sessionIdCookie.toString());
        return "redirect:/";
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

        SessionIdDto sessionIdDto = new SessionIdDto(sessionId);
        sessionService.deactivateSession(sessionIdDto);
        return "redirect:/auth/sign-in";
    }
}
