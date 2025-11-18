package org.weather.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.UserLoginDto;
import org.weather.dto.UserRegistrationDto;

@Controller
@RequestMapping("/auth/sign-in")
public class LoginController {

    @GetMapping()
    public String showSignUpForm(@ModelAttribute("userDto") UserLoginDto userDto) {
        return "auth/sign-in";
    }

    @PostMapping()
    public String showSignInForm(@ModelAttribute("userDto") @Valid UserLoginDto userDto,
                                 BindingResult bindingResult) {
        if(userDto.getPassword().equals("z")) {
            bindingResult.reject("", "Invalid username or password");
        }
        return "auth/sign-in";
    }
}
