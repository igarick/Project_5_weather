package org.weather.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.UserDto;

@Controller
@RequestMapping("/auth")
public class AuthController {

//    @GetMapping("/sign-up")
//    public String showSignUpForm(@ModelAttribute("userDto") UserDto userDto) {
//        return "auth/sign-up";
//    }

    @GetMapping("/sign-up")
    public String showSignUpForm() {
        return "auth/sign-upORIG";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute("userDto") @Valid UserDto userDto,
                                BindingResult bindingResult
    ) {
//        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
//            bindingResult.rejectValue("repeatPassword", "","Passwords don't match");
//        }


        return "auth/sign-up";
    }
}
