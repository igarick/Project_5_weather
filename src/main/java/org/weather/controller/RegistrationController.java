package org.weather.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.InputUserRegistrationDto;
import org.weather.exception.DuplicateUserException;
import org.weather.service.UserService;

@Controller
@RequestMapping("/auth/sign-up")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignUpForm(@ModelAttribute("userDto") InputUserRegistrationDto userDto) {
        return "auth/sign-up";
    }

    @PostMapping()
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
}
