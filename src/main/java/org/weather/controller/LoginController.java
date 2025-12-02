package org.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.InputUserLoginDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.CookieValidationException;
import org.weather.exception.InvalidUserOrPasswordException;
import org.weather.model.Session;
import org.weather.service.SessionService;
import org.weather.service.UserService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth/sign-in")
public class LoginController {
    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;
    private final SessionService sessionService;
    private final CookieParamValidatorAndHandler validatorAndHandler;

    @Autowired
    public LoginController(UserService userService, SessionService sessionService, CookieParamValidatorAndHandler validatorAndHandler) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.validatorAndHandler = validatorAndHandler;
    }

    @GetMapping()
    public String showSignUpForm(@ModelAttribute("userDto") InputUserLoginDto userDto) {
        log.info("Enter the get mapping");
        return "auth/sign-in";
    }

    @PostMapping()
    public String showSignInForm(@ModelAttribute("userDto") @Valid InputUserLoginDto userDto,
                                 BindingResult bindingResult,
                                 @CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                                 HttpServletResponse response
    ) {

        // валидация ввода
        if (bindingResult.hasErrors()) {
            return "auth/sign-in";
        }

//        UUID sessionIdFromCookies = null;
//        if (StringUtils.hasText(sessionIdParam)) {
//            try {
//                sessionIdFromCookies = UUID.fromString(sessionIdParam);
//            } catch (IllegalArgumentException e) {
//                log.warn("Invalid sessionId cookie {}", sessionIdFromCookies);
//                sessionIdFromCookies = null;
//            }
//        }

        UUID sessionIdFromCookies = null;
        try {
            sessionIdFromCookies = validatorAndHandler.extractSessionId(sessionIdParam);
        } catch (CookieValidationException e) {
            log.warn("Invalid sessionId cookie {}", sessionIdFromCookies);
            sessionIdFromCookies = null;
        }

        // аутентификация пользователя
        UserIdDto userId;
        try {
            userId = userService.authenticateUser(userDto);
        } catch (InvalidUserOrPasswordException e) {
            bindingResult.reject("", e.getErrorInfo().getMessage());
            return "auth/sign-in";
        }

        // проверка сессии
        SessionIdDto currentSessionIdDto;
        if (sessionIdFromCookies == null) {
            currentSessionIdDto = sessionService.createSession(userId);
        } else {
            Optional<SessionIdDto> currentOptional = sessionService.findCurrentSession(new SessionIdDto(sessionIdFromCookies));
            UserIdDto finalUserId = userId;
            currentSessionIdDto = currentOptional.orElseGet(() -> sessionService.createSession(finalUserId));


//            if (currentSessionOptional.isEmpty()) {
//                currentSessionIdDto = sessionService.createSession(userId);
//            } else {
//                currentSessionIdDto = currentSessionOptional.get();
//            }
        }

//        SessionIdDto sessionIdFromCookiesDto = new SessionIdDto(sessionIdFromCookies);
//        SessionIdDto currentSessionIdDto = null;
//        Optional<SessionIdDto> currentSessionOptional = sessionService.findCurrentSession(sessionIdFromCookiesDto);
//        if (currentSessionOptional.isEmpty()) {
//            currentSessionIdDto = sessionService.createSession(userId);
//        } else {
//            currentSessionIdDto = currentSessionOptional.get();
//        }


//        SessionIdDto sessionIdDto = null;
//        Optional<SessionIdDto> sessionIdDtoOptional = sessionService.findCurrentSession(sessionIdParam);
//        if(sessionIdDtoOptional.isEmpty()) {
//            sessionIdDto = sessionService.createSession(userId);
//        } else {
//            sessionIdDto = sessionIdDtoOptional.get();
//        }


        // установка кукиз
        String sessionIdStr = String.valueOf(currentSessionIdDto.getSessionId());
//        String sessionId = String.valueOf(sessionIdDto.getSessionId());

        ResponseCookie sessionIdCookie = ResponseCookie.from("sessionId", sessionIdStr)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 1)
                .build();

        response.addHeader("Set-Cookie", sessionIdCookie.toString());

        return "redirect:/search-results";
    }
}
