package org.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.CookieDto;
import org.weather.dto.InputUserLoginDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.InvalidUserOrPasswordException;
import org.weather.model.Session;
import org.weather.service.SessionService;
import org.weather.service.UserService;

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

        System.out.println(sessionIdParam);

        // валидация ввода
        if (bindingResult.hasErrors()) {
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
        // если есть - то беру, если нет - создаю
        SessionIdDto sessionIdDto = sessionService.getSession(userId, sessionIdParam);

        // установка кукиз
        String sessionId = String.valueOf(sessionIdDto.getSessionId());

        ResponseCookie sessionIdCookie = ResponseCookie.from("sessionId", sessionId)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 10)
                .build();

        response.addHeader("Set-Cookie", sessionIdCookie.toString());

//        Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
//        sessionIdCookie.setPath("/");
//        sessionIdCookie.setHttpOnly(true);
//        sessionIdCookie.setMaxAge(60 * 2);
//
//        response.addCookie(sessionIdCookie);


        return "redirect:/auth/sign-in";
    }
}
