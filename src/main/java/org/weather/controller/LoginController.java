package org.weather.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.InputUserLoginDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.InvalidUserOrPasswordException;
import org.weather.service.SessionService;
import org.weather.service.UserService;

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
                                 BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "auth/sign-in";
        }

        UserIdDto userId = null;
        try {
            userId = userService.authenticateUser(userDto);
        } catch (InvalidUserOrPasswordException e) {
            bindingResult.reject("", e.getErrorInfo().getMessage());
        }

        sessionService.getSession(userId);

        return "auth/sign-in";
    }
}
