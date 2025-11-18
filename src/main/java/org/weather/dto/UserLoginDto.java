package org.weather.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserLoginDto {

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Login must be between 2 and 50 characters")
    private String name;

    private String password;
}
