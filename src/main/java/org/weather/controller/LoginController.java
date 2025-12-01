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
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.InvalidUserOrPasswordException;
import org.weather.model.Session;
import org.weather.service.SessionService;
import org.weather.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth/sign-in")
public class LoginController {
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public LoginController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping()
    public String showSignUpForm(@ModelAttribute("userDto") InputUserLoginDto userDto) {
        return "auth/sign-in";
    }

    @PostMapping()
    public String showSignInForm(@ModelAttribute("userDto") @Valid InputUserLoginDto userDto,
                                 BindingResult bindingResult,
                                 @CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                                 HttpServletResponse response
    ) {

        // валидация ввода
        if (bindingResult.hasErrors() || !StringUtils.hasText(sessionIdParam)) {
            return "auth/sign-in";
        }

        UUID sessionIdFromCookies = null;
        try {
            sessionIdFromCookies = UUID.fromString(sessionIdParam);
        } catch (IllegalArgumentException e) {
            return "auth/sign-in";
        }

        // аутентификация пользователя
        UserIdDto userId = null;
        try {
            userId = userService.authenticateUser(userDto);
        } catch (InvalidUserOrPasswordException e) {
            bindingResult.reject("", e.getErrorInfo().getMessage());
            return "auth/sign-in";
        }

        // проверка сессии
        SessionIdDto sessionIdFromCookiesDto = new SessionIdDto(sessionIdFromCookies);
        SessionIdDto currentSessionIdDto = null;
        Optional<SessionIdDto> currentSessionOptional = sessionService.findCurrentSession(sessionIdFromCookiesDto);
        if (currentSessionOptional.isEmpty()) {
            currentSessionIdDto = sessionService.createSession(userId);
        } else {
            currentSessionIdDto = currentSessionOptional.get();
        }


//        SessionIdDto sessionIdDto = null;
//        Optional<SessionIdDto> sessionIdDtoOptional = sessionService.findCurrentSession(sessionIdParam);
//        if(sessionIdDtoOptional.isEmpty()) {
//            sessionIdDto = sessionService.createSession(userId);
//        } else {
//            sessionIdDto = sessionIdDtoOptional.get();
//        }


        // установка кукиз
        String sessionId = String.valueOf(currentSessionIdDto.getSessionId());
//        String sessionId = String.valueOf(sessionIdDto.getSessionId());

        ResponseCookie sessionIdCookie = ResponseCookie.from("sessionId", sessionId)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 10)
                .build();

        response.addHeader("Set-Cookie", sessionIdCookie.toString());

        return "redirect:/auth/sign-in";
    }
}
