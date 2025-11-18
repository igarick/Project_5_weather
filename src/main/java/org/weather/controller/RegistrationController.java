package org.weather.controller;

import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.UserRegistrationDto;
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
    public String showSignUpForm(@ModelAttribute("userDto") UserRegistrationDto userDto) {
        return "auth/sign-up";
    }

    @PostMapping()
    public String processSignUp(@ModelAttribute("userDto") @Valid UserRegistrationDto userDto,
                                BindingResult bindingResult) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.reject("", "Passwords don't match");
            return "auth/sign-up";

        }

        String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());

        try {
            userService.registerUser(userDto.getName(), hashedPassword);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("name", "", "Account with this email already exists");

            return "auth/sign-up";
        }
        //TODO - GO TO MAIN PAGE
        return "auth/sign-up";
    }
}
