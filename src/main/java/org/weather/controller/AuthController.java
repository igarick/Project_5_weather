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

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute("userDto") @Valid UserDto userDto,
                                BindingResult bindingResult
    ) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "","Passwords don't match");
            return "auth/sign-up";
        }


        return "auth/sign-up";
    }


//    @GetMapping("/sign-up")
//    public String showSignUpForm(@ModelAttribute("userDto") UserDto userDto) {
//        return "auth/sign-up";
//

//    }
//    @GetMapping("/sign-up")
//    public String test(Model model) {
//        // сюда кладём переменные для Thymeleaf
//        model.addAttribute("name", "WeatherApp");
//        model.addAttribute("flag", true);
//
//        // Thymeleaf будет искать файл test.html в /WEB-INF/views/
//        return "auth/sign-up";


//    }



//    @GetMapping("/sign-up")
//    public String showSignUpForm() {
    ////        return "auth/sign-up";
//
//        return "auth/sign-up-with-errors";
//    }


}
