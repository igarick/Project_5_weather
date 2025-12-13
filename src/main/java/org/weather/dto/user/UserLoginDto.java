package org.weather.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(

        @NotBlank(message = "Login must not be empty")
        @Size(min = 2, max = 50, message = "Login must be between 2 and 50 characters")
        String login,

        @NotBlank(message = "Password must not be empty")
        @Size(min = 3, max = 8, message = "Password must be between 3 and 8 characters")
        String password) {
}
