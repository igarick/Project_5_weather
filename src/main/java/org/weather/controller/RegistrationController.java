package org.weather.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.UserRegistrationDto;

@Controller
@RequestMapping("/auth/sign-up")
public class RegistrationController {

    @GetMapping
    public String showSignUpForm(@ModelAttribute("userDto") UserRegistrationDto userDto) {
        return "auth/sign-up";
    }

//    @GetMapping()
//    public String showSignUpForm(Model model) {
//        model.addAttribute("userDto", new UserDto());
//        return "auth/sign-up";
//    }

    @PostMapping()
    public String processSignUp(@ModelAttribute("userDto") @Valid UserRegistrationDto userDto,
                                BindingResult bindingResult) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.reject("", "Passwords don't match");
            return "auth/sign-up";
        }

        return "auth/sign-up";
    }
}
