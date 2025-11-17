package org.weather.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Login must be between 2 and 50 characters")
    private String name;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 3, max = 8, message = "Password must be between 3 and 8 characters")
    private String password;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 3, max = 8, message = "Password must be between 3 and 8 characters")
    private String confirmPassword;
}
